package com.github.xiaofu.demo.parquet;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Calendar;

import org.apache.hadoop.fs.Path;
import org.junit.Before;
import org.junit.Test;

import com.github.xiaofu.demo.parquet.ConvertUtils;

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
				"/user/hive/warehouse/view_down_infos/year=2015/month=4/day=13/catalog=1/virtual=0/4e4adb1915f31fad-45b1fdfc89b18c86_754349215_data.0");
		ConvertUtils.convertParquetToCSV(inputFile, ouputFile);
	}
	 


}
