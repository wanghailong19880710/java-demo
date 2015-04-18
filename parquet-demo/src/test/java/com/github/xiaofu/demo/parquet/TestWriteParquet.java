/**
 * 
 */
package com.github.xiaofu.demo.parquet;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
/**
 * @author xiaofu
 *
 */
public class TestWriteParquet {
	File baseDir=new File("E:\\open-source-projects\\big-datas\\parquet\\src\\parquet-compatibility-master\\parquet-testdata\\tpch");
	 
	@Test
	public void writeParquetWithSnappyAndDict() throws IOException
	{
		File inputFile=new File(baseDir,"customer.csv");
		File outFile=new File("mycustomer.parquet");
		MyConvertUtils.convertCsvToParquet(inputFile, outFile, true);
	}
}
