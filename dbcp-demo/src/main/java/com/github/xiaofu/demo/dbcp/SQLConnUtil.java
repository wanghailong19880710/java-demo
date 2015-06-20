/**
 * 
 */
package com.github.xiaofu.demo.dbcp;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;

public class SQLConnUtil {

	private static InputStream inStream;
	private static Properties pro;
	private static DataSource datasource;

	static {
		inStream = SQLConnUtil.class.getClass().getResourceAsStream(
				"/dbcpconfig.properties");
		pro = new Properties();
		try {
			pro.load(inStream);
			datasource = BasicDataSourceFactory.createDataSource(pro);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("初始化错误！");
		}
	}

	public static DataSource getDataSource() {
		return datasource;
	}

	public static Connection getConnection() {
		try {
			return datasource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("得到数据库连接失败！");
		}
	}

	public static void free(ResultSet rs, Statement sta, Connection conn) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (sta != null) {
					sta.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		Connection conn = getConnection();
		System.out.println(conn);
	}
}
