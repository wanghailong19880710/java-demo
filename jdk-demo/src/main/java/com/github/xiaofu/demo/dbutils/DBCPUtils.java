package com.github.xiaofu.demo.dbutils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;

 
public class DBCPUtils {

	private static DataSource dataSource;
	
	static
	{
		 InputStream in = ClassLoader.getSystemResourceAsStream("dbcp.properties");
         Properties p = new Properties();
         try {
			p.load(in);
		} catch (IOException e) {
			 
			e.printStackTrace();
		}
         //创建数据源
         try {
			dataSource = BasicDataSourceFactory.createDataSource(p);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static DataSource getDataSource()
	{
		return dataSource;
	}
}
