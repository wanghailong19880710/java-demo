package com.github.xiaofu.demo.hadoop;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionInputStream;
import org.apache.hadoop.io.compress.CompressionOutputStream;
import org.apache.hadoop.util.ReflectionUtils;

public class BufferedStreamDecompression {
	private static final int BUFFERED_SIZES=1024*1024*1;
	public static void main(String[] args) throws ClassNotFoundException, IOException
	{
		compressionOutput();
		decompressionInputNoBuffered() ;
		decompressionInputWithBuffered();
	}
	private static CompressionCodec getCodec() throws ClassNotFoundException
	{
		String codecClassname="org.apache.hadoop.io.compress.GzipCodec";
		Class<?> codecClass = Class.forName(codecClassname);
		Configuration conf = new Configuration();
		CompressionCodec codec = (CompressionCodec) ReflectionUtils
				.newInstance(codecClass, conf);
		return codec;
	}
	private static void decompressionInputWithBuffered() throws IOException, ClassNotFoundException
	{
		FileInputStream fileInputStream =new FileInputStream("test.zip");
		CompressionCodec codec=getCodec();
		CompressionInputStream decompressionInpuStream = codec.createInputStream(fileInputStream);
		BufferedInputStream bufferedStream=new BufferedInputStream(decompressionInpuStream,BUFFERED_SIZES);
		FileOutputStream fileOutputStream=new FileOutputStream("buffered-upzip.pdf");
		byte[] bytes=new byte[BUFFERED_SIZES];
		int realReadByteCounts=bufferedStream.read(bytes);
		while(realReadByteCounts!=-1)
		{
			System.out.println("nobuffered read byte counts:"+realReadByteCounts);
			fileOutputStream.write(bytes, 0, realReadByteCounts);
			realReadByteCounts=bufferedStream.read(bytes);
		}
		bufferedStream.close();
		fileOutputStream.close();
		 
	}
	private static void decompressionInputNoBuffered() throws IOException, ClassNotFoundException
	{
		FileInputStream fileInputStream =new FileInputStream("test.zip");
		CompressionCodec codec=getCodec();
		CompressionInputStream input = codec.createInputStream(fileInputStream);
		
		FileOutputStream fileOutputStream=new FileOutputStream("nobuffered-upzip.pdf");
		byte[] bytes=new byte[BUFFERED_SIZES];
		int realReadByteCounts=input.read(bytes);
		while(realReadByteCounts!=-1)
		{
			System.out.println("nobuffered read byte counts:"+realReadByteCounts);
			fileOutputStream.write(bytes, 0, realReadByteCounts);
			realReadByteCounts=input.read(bytes);
		}
		input.close();
		fileOutputStream.close();
		 
	}
	private static void compressionOutput() throws ClassNotFoundException, IOException
	{
		String codecClassname="org.apache.hadoop.io.compress.GzipCodec";
		Class<?> codecClass = Class.forName(codecClassname);
		Configuration conf = new Configuration();
		CompressionCodec codec = (CompressionCodec) ReflectionUtils
				.newInstance(codecClass, conf);
		FileOutputStream fileOutputStream=new FileOutputStream("test.zip");
		CompressionOutputStream out = codec.createOutputStream(fileOutputStream);
		FileInputStream fileInputStream=new FileInputStream("ASP.NET 3.5 揭秘中文版(卷1).pdf");
		IOUtils.copyBytes(fileInputStream, out, 4096);
		out.close();
		fileInputStream.close();
		
	}
}
