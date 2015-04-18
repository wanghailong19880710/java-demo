/**
 * Copyright 2012 Twitter, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.xiaofu.demo.parquet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import java.util.Arrays;
import java.util.Calendar;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;



import com.github.xiaofu.demo.parquet.Utils;

import parquet.Log;
import parquet.Preconditions;
import parquet.column.page.PageReadStore;
import parquet.example.data.Group;
import parquet.example.data.simple.NanoTime;
import parquet.example.data.simple.SimpleGroup;
import parquet.example.data.simple.convert.GroupRecordConverter;
import parquet.hadoop.ParquetFileReader;
import parquet.hadoop.ParquetReader;
import parquet.hadoop.example.GroupReadSupport;
import parquet.hadoop.metadata.CompressionCodecName;
import parquet.hadoop.metadata.ParquetMetadata;
import parquet.io.ColumnIOFactory;
import parquet.io.MessageColumnIO;
import parquet.io.RecordReader;
import parquet.io.api.Binary;
import parquet.schema.MessageType;
import parquet.schema.MessageTypeParser;

public class MyConvertUtils
{
	private static final int BLOCK_SIZE = 1024 * 1024 * 1024;
	private static final int PAGE_SIZE = 10 * 1024;
	private static final Log LOG = Log.getLog(MyConvertUtils.class);

	public static final String CSV_DELIMITER = "|";

	private static String readFile(String path) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(path));
		StringBuilder stringBuilder = new StringBuilder();

		try
		{
			String line = null;
			String ls = System.getProperty("line.separator");

			while ((line = reader.readLine()) != null)
			{
				stringBuilder.append(line);
				stringBuilder.append(ls);
			}
		}
		finally
		{
			Utils.closeQuietly(reader);
		}

		return stringBuilder.toString();
	}

	public static String getSchema(File csvFile) throws IOException
	{
		String fileName = csvFile.getName().substring(0,
				csvFile.getName().length() - ".csv".length())
				+ ".schema";
		File schemaFile = new File(csvFile.getParentFile(), fileName);
		return readFile(schemaFile.getAbsolutePath());
	}

	public static void convertCsvToParquet(File csvFile, File outputParquetFile)
			throws IOException
	{
		convertCsvToParquet(csvFile, outputParquetFile, false);
	}

	public static void convertCsvToParquet(File csvFile,
			File outputParquetFile, boolean enableDictionary)
			throws IOException
	{
		LOG.info("Converting " + csvFile.getName() + " to "
				+ outputParquetFile.getName());
		String rawSchema = getSchema(csvFile);
		if (outputParquetFile.exists())
		{
			throw new IOException("Output file "
					+ outputParquetFile.getAbsolutePath() + " already exists");
		}

		Path path = new Path(outputParquetFile.toURI());

		MessageType schema = MessageTypeParser.parseMessageType(rawSchema);
		MyCsvParquetWriter writer = new MyCsvParquetWriter(path, schema,
				enableDictionary);

		BufferedReader br = new BufferedReader(new FileReader(csvFile));
		String line;
		int lineNumber = 0;
		try
		{
			while ((line = br.readLine()) != null)
			{
				String[] fields = line.split(Pattern.quote(CSV_DELIMITER));
				writer.write(Arrays.asList(fields));
				++lineNumber;
			}

			writer.close();
		}
		finally
		{
			LOG.info("Number of lines: " + lineNumber);
			Utils.closeQuietly(br);
		}
	}

	public static void convertCsvToParquet(File csvFile,
			Path outputParquetFile, boolean enableDictionary)
			throws IOException
	{
		LOG.info("Converting " + csvFile.getName() + " to "
				+ outputParquetFile.getName());
		String rawSchema = getSchema(csvFile);

		MessageType schema = MessageTypeParser.parseMessageType(rawSchema);
		MyCsvParquetWriter writer = new MyCsvParquetWriter(outputParquetFile,
				schema, CompressionCodecName.GZIP, BLOCK_SIZE, PAGE_SIZE,
				enableDictionary);

		BufferedReader br = new BufferedReader(new FileReader(csvFile));
		String line;
		int lineNumber = 0;
		try
		{
			while ((line = br.readLine()) != null)
			{
				String[] fields = line.split(Pattern.quote(CSV_DELIMITER));
				writer.write(Arrays.asList(fields));
				++lineNumber;
			}

			writer.close();
		}
		finally
		{
			LOG.info("Number of lines: " + lineNumber);
			Utils.closeQuietly(br);
		}
	}

	public static void convertParquetToCSV(Path parquetFilePath,
			File csvOutputFile) throws IOException
	{
		// Preconditions.checkArgument(parquetFilePath.getName().endsWith(".parquet"),
		// "parquet file should have .parquet extension");
		Preconditions.checkArgument(csvOutputFile.getName().endsWith(".csv"),
				"csv file should have .csv extension");
		// Preconditions.checkArgument(!csvOutputFile.exists(),
		// "Output file " + csvOutputFile.getAbsolutePath() +
		// " already exists");

		LOG.info("Converting " + parquetFilePath.getName() + " to "
				+ csvOutputFile.getName());

		Configuration configuration = new Configuration(true);

		GroupReadSupport readSupport = new GroupReadSupport();
		ParquetMetadata readFooter = ParquetFileReader.readFooter(
				configuration, parquetFilePath);
		MessageType schema = readFooter.getFileMetaData().getSchema();

		readSupport.init(configuration, null, schema);
		BufferedWriter w = new BufferedWriter(new FileWriter(csvOutputFile));
		ParquetReader<Group> reader = new ParquetReader<Group>(parquetFilePath,
				readSupport);
		try
		{
			Group g = null;
			while ((g = reader.read()) != null)
			{
				writeGroup(w, g, schema);
			}
			reader.close();
		}
		finally
		{
			Utils.closeQuietly(w);
		}
	}

	public static void convertParquetToCSV(File parquetFile, File csvOutputFile)
			throws IOException
	{
		Preconditions.checkArgument(parquetFile.getName().endsWith(".parquet"),
				"parquet file should have .parquet extension");
		Preconditions.checkArgument(csvOutputFile.getName().endsWith(".csv"),
				"csv file should have .csv extension");
		Preconditions.checkArgument(!csvOutputFile.exists(), "Output file "
				+ csvOutputFile.getAbsolutePath() + " already exists");

		LOG.info("Converting " + parquetFile.getName() + " to "
				+ csvOutputFile.getName());

		Path parquetFilePath = new Path(parquetFile.toURI());

		Configuration configuration = new Configuration(true);

		GroupReadSupport readSupport = new GroupReadSupport();
		ParquetMetadata readFooter = ParquetFileReader.readFooter(
				configuration, parquetFilePath);
		MessageType schema = readFooter.getFileMetaData().getSchema();

		readSupport.init(configuration, null, schema);
		BufferedWriter w = new BufferedWriter(new FileWriter(csvOutputFile));
		ParquetReader<Group> reader = new ParquetReader<Group>(parquetFilePath,
				readSupport);
		try
		{
			Group g = null;
			while ((g = reader.read()) != null)
			{
				writeGroup(w, g, schema);
			}
			reader.close();
		}
		finally
		{
			Utils.closeQuietly(w);
		}
	}

	private static void writeGroup(BufferedWriter w, Group g, MessageType schema)
			throws IOException
	{
		for (int j = 0; j < schema.getFieldCount(); j++)
		{
			if (j > 0)
			{
				w.write(CSV_DELIMITER);
			}
			String fieldName = schema.getFieldName(j);
			String valueToString = "";
			if (fieldName.equalsIgnoreCase("visit_time"))
			{

				
				Binary binary = g.getInt96(j, 0);
				byte[] invertBytes = getInvertBytes(binary.getBytes());
				
				NanoTime nanoSeconds =NanoTime.fromBinary(Binary.fromByteArray(invertBytes));

				System.out.println("====nanoTime=======");
				System.out.println(nanoSeconds.toString());
			    valueToString=GetDateTimeFormat(nanoSeconds);
				System.out.println("=========result time=========");
				System.out.println(valueToString);
				System.out.println("==================");
				
			}
			else
				valueToString = g.getValueToString(j, 0);
			w.write(valueToString);
		}
		w.write('\n');
	}

	private static String GetDateTimeFormat(NanoTime time)
	{
		
		double JD = time.getJulianDay();
		double Z = Math.floor(JD + 0.5);
		double W = Math.floor((Z - 1867216.25) / 36524.25);
		double X = Math.floor(W / 4);
		double AA = Math.floor(Z + 1 + W - X);
		double BB = Math.floor(AA + 1524);
		double CC = Math.floor((BB - 122.1) / 365.25);
		double DD = Math.floor(365.25 * CC);
		double EE = Math.floor((BB - DD) / 30.6001);
		double FF = Math.floor(30.6001 * EE);

		double day = BB - DD - FF;
		double month;
		double year;

		if ((EE - 13) <= 12 && (EE - 13) > 0)
			month = EE - 13;
		else
			month = EE - 1;

		if (month == 1 || month == 2)
			year = CC - 4715;
		else
			year = CC - 4716;
	 
		long ms = time.getTimeOfDayNanos()/1000000;
		int ss = 1000;
		int mi = ss * 60;
		int hh = mi * 60;
		 
		int hour =(int) ms / hh;
		long minutes = (ms -  hour * hh) / mi;
		long seconds = (ms -  hour * hh - minutes * mi) / ss;
		long milliSecond = ms -  hour * hh - minutes * mi - seconds * ss;
		 
		return String.format("%s-%s-%s %s:%s:%s", (int)year,(int)month,(int)day,hour,minutes,seconds);
	 
		
	}

	private static byte[] getInvertBytes(byte[] bytes)
	{
		byte[] newBytes = new byte[bytes.length];
		for (int i = 0; i < bytes.length; i++)
		{
			newBytes[i] = bytes[bytes.length - 1 - i];

		}
		return newBytes;
	}

	@Deprecated
	public static void convertParquetToCSVEx(File parquetFile,
			File csvOutputFile) throws IOException
	{
		Preconditions.checkArgument(parquetFile.getName().endsWith(".parquet"),
				"parquet file should have .parquet extension");
		Preconditions.checkArgument(csvOutputFile.getName().endsWith(".csv"),
				"csv file should have .csv extension");
		Preconditions.checkArgument(!csvOutputFile.exists(), "Output file "
				+ csvOutputFile.getAbsolutePath() + " already exists");

		LOG.info("Converting " + parquetFile.getName() + " to "
				+ csvOutputFile.getName());

		Path parquetFilePath = new Path(parquetFile.toURI());

		Configuration configuration = new Configuration(true);

		// TODO Following can be changed by using ParquetReader instead of
		// ParquetFileReader
		ParquetMetadata readFooter = ParquetFileReader.readFooter(
				configuration, parquetFilePath);
		MessageType schema = readFooter.getFileMetaData().getSchema();
		ParquetFileReader parquetFileReader = new ParquetFileReader(
				configuration, parquetFilePath, readFooter.getBlocks(),
				schema.getColumns());
		BufferedWriter w = new BufferedWriter(new FileWriter(csvOutputFile));
		PageReadStore pages = null;
		try
		{
			while (null != (pages = parquetFileReader.readNextRowGroup()))
			{
				final long rows = pages.getRowCount();
				LOG.info("Number of rows: " + rows);

				final MessageColumnIO columnIO = new ColumnIOFactory()
						.getColumnIO(schema);
				final RecordReader<Group> recordReader = columnIO
						.getRecordReader(pages,
								new GroupRecordConverter(schema));
				for (int i = 0; i < rows; i++)
				{
					final Group g = recordReader.read();
					writeGroup(w, g, schema);
				}
			}
		}
		finally
		{
			Utils.closeQuietly(parquetFileReader);
			Utils.closeQuietly(w);
		}
	}

}
