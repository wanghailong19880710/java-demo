package com.github.xiaofu.demo.jtds;

import java.sql.Connection;
import java.sql.SQLException;

import net.sourceforge.jtds.jdbc.Driver;
import net.sourceforge.jtds.jdbcx.JtdsDataSource;

public class JtdsDemo {

	public static void main(String[] args) {

		try {
			//Class.forName("net.sourceforge.jtds.jdbc.Driver");
			//仅仅是取代了DriverMangaer，底层还是用Driver实现
			JtdsDataSource ds = new JtdsDataSource();
			ds.setInstance("MSSQL2008");
			ds.setPassword("sa");
			ds.setUser("sa");		 
			ds.setServerName("127.0.0.1");
			ds.setServerType(Driver.SQLSERVER);
			Connection conn=ds.getConnection();
		    
			System.out.println(conn);
		 
		} catch (SQLException e) {
			e.printStackTrace();

		}
	}

}
