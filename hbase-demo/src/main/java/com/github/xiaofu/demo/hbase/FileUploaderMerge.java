/**
 * 
 */
package com.github.xiaofu.demo.hbase;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * @author fulaihua
 *
 */
public class FileUploaderMerge {
	public static Configuration conf = null;
	private static HTable onlineTable;
	private static HTable localTable;
	public static String TABLE = "test_flh";
	static {
		conf = HBaseConfiguration.create();
		try {

			conf.set("hbase.zookeeper.quorum",
					"node203.vipcloud,node204.vipcloud,node205.vipcloud");
			localTable = new HTable(conf,TABLE);
			/*
			 * conf.set( "hbase.zookeeper.quorum",
			 * "node600.vipcloud,node601.vipcloud,node602.vipcloud,node603.vipcloud,node604.vipcloud"
			 * ); onlineTable = new HTable(conf, "file_uploader");
			 */
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		HBaseAdmin admin = new HBaseAdmin(conf);
		admin.disableTable(TABLE);
		try {
			Configuration fsConf=new Configuration();
			fsConf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
			HMerge.merge(conf, FileSystem.get(fsConf),
					Bytes.toBytes(TABLE),null,null);
		} finally {
			admin.enableTable(TABLE);
			admin.close();
		}

	}

}
