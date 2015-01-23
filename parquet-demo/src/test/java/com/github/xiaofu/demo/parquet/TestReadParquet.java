package com.github.xiaofu.demo.parquet;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Calendar;

import org.apache.hadoop.fs.Path;
import org.junit.Before;
import org.junit.Test;

import parquet.compat.test.ConvertUtils;
import parquet.example.data.simple.NanoTime;

public class TestReadParquet
{

	@Before
	public void setUp() throws Exception
	{
	}


	@Test
	public void testHdfsToFile() throws IOException
	{
		File ouputFile = new File(
				"customer.impala.csv");
		Path inputFile = new Path(
				"/user/hive/warehouse/customer_parquet/customer.parquet");
		ConvertUtils.convertParquetToCSV(inputFile, ouputFile);
	}
	@Test
	public void test_Convert_Search_Click_infos_To_Csv_File() throws IOException
	{
		File ouputFile = new File(
				"search_click_infos.csv");
		Path inputFile = new Path(
				"/user/hive/warehouse/search_click_infos/year=2015/month=1/day=1/d449a97a5029d53b-b51236f9c6a1e1ab_1408550508_data.0");
		ConvertUtils.convertParquetToCSV(inputFile, ouputFile);
	}
	 


}
