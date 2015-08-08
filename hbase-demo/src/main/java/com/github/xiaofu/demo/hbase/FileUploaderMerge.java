/**
 * 
 */
package com.github.xiaofu.demo.hbase;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.Writables;

/**
 * @author fulaihua
 *
 */
public class FileUploaderMerge {
	public static Configuration conf = null;
	private static HTable onlineTable;
	private static HTable localTable;
	public static String TABLE = "test_uploader";
	static {
		conf = HBaseConfiguration.create();
		try {

			conf.set("hbase.zookeeper.quorum",
					"node203.vipcloud,node204.vipcloud,node205.vipcloud");
			
		} catch (Exception e) {

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
		//scanTableRegionFromMeta();

	}
	public static void scanTableRegionFromMeta() throws IOException
	{
		Scan scan=new Scan();
		//scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("regioninfo"));
		scan.addFamily( HConstants.CATALOG_FAMILY);
		scan.setStartRow(HRegionInfo.createRegionName(Bytes.toBytes(TABLE),
				HConstants.EMPTY_START_ROW,
				Bytes.toBytes(HConstants.ZEROES), false));
		
		HTable table=new HTable(conf, HConstants.META_TABLE_NAME);
		ResultScanner scanner= table.getScanner(scan);
		try {
			
			for (Result result : scanner) {
			   System.out.println((HRegionInfo) Writables.getWritable(
					  result.getValue(HConstants.CATALOG_FAMILY, HConstants.REGIONINFO_QUALIFIER), new HRegionInfo()));
				//System.out.println(result.toString());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally
		{
			scanner.close();
			table.close();
		}
	}

}
