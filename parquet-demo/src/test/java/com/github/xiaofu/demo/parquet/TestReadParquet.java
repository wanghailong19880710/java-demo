package com.github.xiaofu.demo.parquet;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Calendar;

import org.apache.hadoop.fs.Path;
import org.junit.Before;
import org.junit.Test;

import com.github.xiaofu.demo.parquet.MyConvertUtils;

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
		MyConvertUtils.convertParquetToCSV(inputFile, ouputFile);
	}
	@Test
	public void test_Convert_Search_Click_infos_To_Csv_File() throws IOException
	{
		File ouputFile = new File(
				"view.csv");
		Path inputFile = new Path(
				"/user/hive/warehouse/view_down_infos/year=2015/month=5/day=27/catalog=1/virtual=0/73313b6b3e744c43aac76cec4a383354");
		MyConvertUtils.convertParquetToCSV(inputFile, ouputFile);
	}
	@Test
	public void convertParquetToCSV_With_Specified_Columns_And_Column_Filter() throws IOException
	{
		File ouputFile = new File(
				"view.csv");
		Path inputFile = new Path(
				"/user/hive/warehouse/view_down_infos/year=2015/month=5/day=27/catalog=1/virtual=0/73313b6b3e744c43aac76cec4a383354");
		MyConvertUtils.convertParquetToCSV_With_Specified_Columns_And_Column_Filter(inputFile, ouputFile);
	}


}
