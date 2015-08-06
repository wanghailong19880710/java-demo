/**
 * 
 */
package com.github.xiaofu.demo.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Row;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.MD5Hash;
import org.apache.hadoop.hbase.util.RegionSplitter;
import org.apache.hadoop.hbase.util.RegionSplitter.SplitAlgorithm;

/**
 * @author fulaihua
 * 
 */
public class ClientOp {
	public static Configuration conf = null;
	private static HTable onlineTable;
	private static HTable localTable;
	static {
		conf = HBaseConfiguration.create();
		try {

			conf.set("hbase.zookeeper.quorum",
					"node203.vipcloud,node204.vipcloud,node205.vipcloud");
			localTable = new HTable(conf, "test_flh");
			/*
			 * conf.set( "hbase.zookeeper.quorum",
			 * "node600.vipcloud,node601.vipcloud,node602.vipcloud,node603.vipcloud,node604.vipcloud"
			 * ); onlineTable = new HTable(conf, "file_uploader");
			 */
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	public static String TABLE = "test_flh";

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		// deleteRow(TABLE,"aa");
		//createTable(TABLE, new String[] { "colfam1", "colfam2" });
		// batchTest("test_flh");
		selectRow(TABLE,"row2");
		// exportOnlineToLocal();
		//System.out.println(idToMD5Hash("JG@1494"));
		//inportDataToTest();
		//writeRow(TABLE,"row1","colfam1","qual1","2");
	}

	public static void createTable(String tablename, String[] cfs)
			throws IOException {
		HBaseAdmin admin = new HBaseAdmin(conf);
		if (admin.tableExists(tablename)) {
			System.out.println("表已经存在！");
		} else {
			HTableDescriptor tableDesc = new HTableDescriptor(tablename);
			for (int i = 0; i < cfs.length; i++) {
				tableDesc.addFamily(new HColumnDescriptor(cfs[i]));
			}
			SplitAlgorithm splitAlgor = new RegionSplitter.HexStringSplit();
			admin.createTableWithAutoSplittableQualifier(tableDesc,
					splitAlgor.split(5));
			System.out.println("表创建成功！");
		}
	}

	public static void deleteTable(String tablename) throws IOException {
		try {
			HBaseAdmin admin = new HBaseAdmin(conf);
			admin.disableTable(tablename);
			admin.deleteTable(tablename);
			System.out.println("表删除成功！");
		} catch (MasterNotRunningException e) {
			e.printStackTrace();
		} catch (ZooKeeperConnectionException e) {
			e.printStackTrace();
		}
	}

	public static void writeRow(String tablename, String rowKey,String family,
			String qualifier, String value) {
		try {
			HTable table = new HTable(conf, tablename);
			Put put = new Put(Bytes.toBytes(rowKey));
			put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier),
					Bytes.toBytes(value));

			table.put(put);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void selectRow(String tablename, String rowKey)
			throws IOException {
		HTable table = new HTable(conf, tablename);
		Get g = new Get(rowKey.getBytes());
		// select all versions of all columns specified row
		g.setMaxVersions();

		Result rs = table.get(g);
		if (rs.raw().length == 0)
			System.out.println("no data");
		for (KeyValue kv : rs.raw()) {
			System.out.println(kv);

		}
	}

	public static void deleteRow(String tablename, String rowkey)
			throws IOException {
		HTable table = new HTable(conf, tablename);
		List<Delete> list = new ArrayList<Delete>();
		Delete d1 = new Delete(rowkey.getBytes());
		// d1.deleteColumn("bb".getBytes(), "dd".getBytes());
		list.add(d1);
		table.delete(list);
		System.out.println("删除行成功！");
	}

	/**
	 * get all rows,all columns latest version
	 * 
	 * @param tablename
	 */
	public static void scaner(String tablename) {
		try {
			HTable table = new HTable(conf, tablename);
			Scan s = new Scan();

			ResultScanner rs = table.getScanner(s);

			for (Result r : rs) {
				KeyValue[] kv = r.raw();
				for (int i = 0; i < kv.length; i++) {

					System.out.print(new String(kv[i].getRow()) + "");
					System.out.print(new String(kv[i].getFamily()) + ":");
					System.out.print(new String(kv[i].getQualifier()) + "");
					System.out.print(kv[i].getTimestamp() + "");
					System.out.println(new String(kv[i].getValue()));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void batchTest(String tablename) throws IOException {
		HTable table = new HTable(conf, tablename);
		byte[] ROW1 = Bytes.toBytes("row1");
		byte[] ROW2 = Bytes.toBytes("row2");
		byte[] COLFAM1 = Bytes.toBytes("colfam1");
		byte[] COLFAM2 = Bytes.toBytes("colfam2");
		byte[] QUAL1 = Bytes.toBytes("qual1");
		byte[] QUAL2 = Bytes.toBytes("qual2");
		List<Row> batch = new ArrayList<Row>();
		Put put = new Put(ROW2);
		put.add(COLFAM2, QUAL1, Bytes.toBytes("val5"));
		batch.add(put);
		Get get1 = new Get(ROW1);
		get1.addColumn(COLFAM1, QUAL1);
		batch.add(get1);
		Delete delete = new Delete(ROW1);
		delete.deleteColumns(COLFAM1, QUAL2);
		batch.add(delete);
		Get get2 = new Get(ROW2);
		get2.addFamily(Bytes.toBytes("BOGUS"));
		batch.add(get2);
		Object[] results = new Object[batch.size()];
		try {
			table.batch(batch, results);
		} catch (Exception e) {
			System.err.println("Error: " + e);
		}
		for (int i = 0; i < results.length; i++) {
			System.out.println("Result[" + i + "]: " + results[i]);
		}
	}

	public static void inportDataToTest() throws IOException {
		List<Put> lists = new ArrayList<Put>();
		for (int i = 0; i < 1000; i++) {
			Put put = new Put(Bytes.toBytes(idToMD5Hash(UUID.randomUUID().toString().replace("-", ""))));
			put.setWriteToWAL(false);
			
			put.add(Bytes.toBytes("colfam1"), Bytes.toBytes("aa"),
					Bytes.toBytes("1"));
			lists.add(put);
		}
		localTable.setAutoFlush(false);
		try {
			localTable.put(lists);
		} finally {
			localTable.flushCommits();
		}
	}

	public static String idToMD5Hash(String idKey) {
		return MD5Hash.getMD5AsHex(Bytes.toBytes(idKey)).substring(0, 8) + ":"
				+ idKey;
	}

	public static void exportOnlineToLocal() throws IOException {
		Scan scan = new Scan();
		scan.setCaching(20);
		scan.setBatch(20);
		ResultScanner scanner = null;
		try {
			scanner = onlineTable.getScanner(scan);
			List<Put> lists = new ArrayList<Put>();
			for (Result result : scanner) {
				for (KeyValue kv : result.list()) {
					System.out.println(kv);
					Put put = new Put();
					put.add(kv);
					lists.add(put);
				}
			}
			// localTable.put(lists);

		} finally {
			if (scanner != null)
				scanner.close();
			onlineTable.close();
			localTable.close();

		}
	}

}
