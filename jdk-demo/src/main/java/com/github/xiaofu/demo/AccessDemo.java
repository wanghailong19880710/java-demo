/**
 * 
 */
package com.github.xiaofu.demo;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;





import com.cqvip.vipcloud.com.healthmarketscience.jackcess.Database.FileFormat;

import net.ucanaccess.jdbc.UcanaccessDriver;

/**
 * @author fulaihua
 *
 */
public class AccessDemo {

	/**
	 * @param args
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		String mdbFileName = System.currentTimeMillis() + "_refvipdb.mdb";
		File mdbFile = new File("/tmp/ref_mdb", mdbFileName);
		if (!mdbFile.getParentFile().exists())
			mdbFile.getParentFile().mkdirs();
		Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
		String url = UcanaccessDriver.URL_PREFIX
				+ mdbFile.getAbsolutePath() + ";newdatabaseversion="
				+ FileFormat.V2003.name();
		Connection ucanaccessConnection = DriverManager.getConnection(url, "", "");
		ucanaccessConnection.setAutoCommit(false);
		Statement statement = ucanaccessConnection.createStatement();
		statement
				.execute("CREATE TABLE refvipdb (refid  text(100),strTitle memo,strType text(5),strName text(255),strPubAddress text(255),strPublishWriter text(120),zz text(255),strYVN text(100),strPages text(200),lngSourceID text(100),lngSourceID_s text(100),strWriter2 text(60),strWriter1 text(60),strDC memo,YVM text(5),strdate text(50),blnDealed Boolean,flag text(1),LngID Long,qqq boolean,strRefText memo,FZ_Percent text(5),FZ_lngID Long,strVOL text(5),strNUMBER text(5),strBEGINPAGE text(15),strENDPAGE text(15),strJUMPPAGE text(100),blnErr boolean,style LONG)");
		ucanaccessConnection.commit();
		ucanaccessConnection.close();
	}

}
