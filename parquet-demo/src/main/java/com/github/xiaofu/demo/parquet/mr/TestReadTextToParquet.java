/**
 * @ProjectName: parquet-demo
 * @Copyright: 版权所有 Copyright © 2001-2012 cqvip.com Inc. All rights reserved. 
 * @address: http://www.cqvip.com
 * @date: 2015年6月3日 下午1:14:19
 * @Description: 本内容仅限于维普公司内部使用，禁止转发.
 */
package com.github.xiaofu.demo.parquet.mr;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.MRJobConfig;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.github.xiaofu.demo.parquet.Utils;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

import parquet.Log;
import parquet.column.ColumnDescriptor;
import parquet.example.data.Group;
import parquet.example.data.simple.NanoTime;
import parquet.example.data.simple.SimpleGroup;
import parquet.hadoop.example.ExampleOutputFormat;
import parquet.hadoop.example.GroupWriteSupport;
import parquet.hadoop.metadata.CompressionCodecName;
import parquet.io.api.Binary;
import parquet.schema.MessageType;
import parquet.schema.MessageTypeParser;

/**
 * <p>
 * </p>
 * 
 * @author flh 2015年6月3日 下午1:14:19
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2015年6月3日
 * @modify by reason:{方法名}:{原因}
 */
public class TestReadTextToParquet extends Configured implements Tool
{
	private static final Log LOG = Log.getLog(TestReadTextToParquet.class);
	private static final String fileSchema = "message schema {   optional binary user_id;   optional int32 user_group_id;   optional int32 user_organ_id;   optional int32 user_area_id;   optional int32 site_id;   optional int32 site_type;   optional int64 site_ip;   optional binary user_os;   optional binary user_browser;   optional binary user_session_id;   optional binary user_ip_address;   optional int64 user_ip;   optional int32 user_ip_range;   optional int32 operate_id;   optional boolean is_success;   optional int96 visit_time;   optional int64 load_page_cost_time;   optional binary page_url;   optional binary page_params;   optional int32 page_id;   optional int32 page_board_id;   optional int32 article_num;   optional binary article_id;   optional int32 article_type;   optional int32 gch5;   optional binary gch;   optional binary full_class_ids;   optional int32 class_id;   optional int32 article_year;   optional int32 article_directory;   optional int32 album_id;   optional binary object_id;   optional binary object_value;   optional binary remark;   optional binary id;   optional int32 type;   optional binary keywords;   optional binary table_name;   optional int32 return_counts;   optional int32 offset_counts;   optional int32 total_counts;   optional binary sort_by;   optional int32 click_position;   optional binary rules;   optional binary filter_rules;   optional binary refer_url;   optional binary options; }";
	private static MessageType messageType;
	private static Splitter splitter;

	/*
	 * Read a Parquet record, write a Parquet record
	 */
	public static class ReadRequestMap extends
			Mapper<LongWritable, Text, Void, Group>
	{
		@Override
		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException
		{
			String path= context.getConfiguration().get(MRJobConfig.MAP_INPUT_FILE);
			if(path==null)
			{
				System.out.println("========================>>"+path);
			}
			if (messageType == null)
			{
				splitter = Splitter.on('|').trimResults();
				messageType = MessageTypeParser.parseMessageType(fileSchema);
			}
			Group group = new SimpleGroup(messageType);
			String[] fields =Iterables.toArray(splitter.split(value.toString()),String.class); 

			List<ColumnDescriptor> columns = messageType.getColumns();
			for (int i = 0; i < columns.size(); i++)
			{
				ColumnDescriptor desc = columns.get(i);
				switch (desc.getType())
				{
				case BINARY:
					group.add(desc.getPath()[0], Binary.fromString(fields[i]));
					break;
				case BOOLEAN:
					group.add(desc.getPath()[0],
							Boolean.parseBoolean(fields[i]));
					break;
				case DOUBLE:
					group.add(desc.getPath()[0], Double.parseDouble(fields[i]));
					break;
				case FIXED_LEN_BYTE_ARRAY:
					break;
				case FLOAT:
					group.add(desc.getPath()[0], Float.parseFloat(fields[i]));
					break;
				case INT32:
					group.add(desc.getPath()[0], Integer.parseInt(fields[i]));
					break;
				case INT64:
					group.add(desc.getPath()[0], Long.parseLong(fields[i]));
					break;
				case INT96:
					Calendar calendar = Calendar.getInstance();
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss.SSS");
					try
					{
						calendar.setTime(sdf.parse(fields[i]));
					}
					catch (ParseException e)
					{

						e.printStackTrace();
					}
					long julian = Utils.dateToJulianDay(
							calendar.get(Calendar.YEAR),
							calendar.get(Calendar.MONTH) + 1,
							calendar.get(Calendar.DATE));
					Calendar zeroCalendar = Calendar.getInstance();
					zeroCalendar.set(calendar.get(Calendar.YEAR),
							calendar.get(Calendar.MONTH),
							calendar.get(Calendar.DATE), 0, 0, 0);
					NanoTime nanoTime = new NanoTime((int) julian,
							(calendar.getTimeInMillis() - zeroCalendar
									.getTimeInMillis()) * 1000 * 1000);
					byte[] invertBytes = Utils.getInvertBytes(nanoTime
							.toBinary().getBytes());

					group.add(desc.getPath()[0],
							Binary.fromByteArray(invertBytes));
					break;
				default:
					break;

				}
			}

			context.write(null, group);
		}
	}

	public int run(String[] args) throws Exception
	{
		if (args.length < 2)
		{
			LOG.error("Usage: " + getClass().getName()
					+ " INPUTFILE OUTPUTFILE [compression]");
			return 1;
		}
		String inputFile = args[0];
		String outputFile = args[1];
		String compression = (args.length > 2) ? args[2] : "none";

		messageType = MessageTypeParser.parseMessageType(fileSchema);
		LOG.info(messageType);
		
		Job job = new Job(getConf());
		job.getConfiguration().set(MRJobConfig.MAP_JAVA_OPTS, "-Xmx4096m");
		job.getConfiguration().set(MRJobConfig.MAP_MEMORY_MB, "4096");
		
		job.setJarByClass(getClass());
		job.setJobName(getClass().getName());
		job.setJar("E:\\open-source-projects\\github\\java-demo-parent\\parquet-demo\\target\\parquet-demo-0.0.1-SNAPSHOT.jar");
		
		job.setMapperClass(ReadRequestMap.class);
		job.setNumReduceTasks(0);
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(ExampleOutputFormat.class);
		
		CompressionCodecName codec = CompressionCodecName.UNCOMPRESSED;
		if (compression.equalsIgnoreCase("snappy"))
		{
			codec = CompressionCodecName.SNAPPY;
		}
		else if (compression.equalsIgnoreCase("gzip"))
		{
			codec = CompressionCodecName.GZIP;
		}
		LOG.info("Output compression: " + codec);
		ExampleOutputFormat.setCompression(job, codec);
		ExampleOutputFormat.setSchema(job, messageType);
 
		TextInputFormat.setInputPaths(job, new Path(inputFile));
		TextInputFormat.setInputDirRecursive(job, true);
		ExampleOutputFormat.setOutputPath(job, new Path(outputFile));
		job.setJobSetupCleanupNeeded(true);
		job.waitForCompletion(true);

		return 0;
	}

	public static void main(String[] args) throws Exception
	{
		try
		{
			System.setProperty("HADOOP_USER_NAME", "vipcloud");
			args = new String[] {
					"/user/hive/warehouse/tmp_view_infos/",
					"/user/hive/warehouse/tmp_view_down_infos/year=2015/month=4/day=16/catalog=1/virtual=0",
					"snappy"
			};
			int res = ToolRunner.run(new Configuration(),
					new TestReadTextToParquet(), args);
			System.exit(res);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(255);
		}
	}

}
