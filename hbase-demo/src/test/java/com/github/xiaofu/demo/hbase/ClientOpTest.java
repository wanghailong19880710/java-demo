package com.github.xiaofu.demo.hbase;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Append;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Increment;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Before;
import org.junit.Test;

public class ClientOpTest {
	private static Configuration conf = null;
	static {
		conf = HBaseConfiguration.create();
	}
	private static String TABLE = "test_flh";

	@Before
	public void setUp() throws Exception {
	}

	// @Test
	public void deleteRow() throws IOException {
		ClientOp.writeRow(ClientOp.TABLE, "row1", "a", "value1");
		ClientOp.writeRow(ClientOp.TABLE, "row1", "b", "value1");
		ClientOp.deleteRow(TABLE, "row1");
	}

	@Test
	public void deteteRowSpecifiedColumnVersion() throws IOException {
		ClientOp.writeRow(ClientOp.TABLE, "row1", "a", "value1");
		ClientOp.writeRow(ClientOp.TABLE, "row1", "a", "value1");
		ClientOp.writeRow(ClientOp.TABLE, "row1", "a", "value1");
		HTable table = new HTable(conf, TABLE);
		List<Delete> list = new ArrayList<Delete>();
		Delete d1 = new Delete("row1".getBytes());
		d1.deleteColumn("cf".getBytes(), "a".getBytes());
		list.add(d1);
		table.delete(list);
		System.out.println("删除行成功！");
		ClientOp.selectRow(TABLE, "row1");
		ClientOp.deleteRow(TABLE, "row1");
	}

	@Test
	public void appendValue() throws IOException {
		ClientOp.writeRow(ClientOp.TABLE, "row1", "a", "value1");
		ClientOp.writeRow(ClientOp.TABLE, "row1", "a", "value1");
		ClientOp.selectRow(TABLE, "row1");
		HTable table = new HTable(conf, TABLE);
		// 历史版本直接删除了，只有最新版本+追加值了
		Append append = new Append("row1".getBytes());
		append.add("cf".getBytes(), "a".getBytes(), "22".getBytes());
		table.append(append);
		System.out.println("====================");
		ClientOp.selectRow(TABLE, "row1");
		ClientOp.deleteRow(TABLE, "row1");
	}

	@Test
	public void incrementValue() throws IOException {

		ClientOp.writeRow(ClientOp.TABLE, "row1", "a", "1");
		ClientOp.writeRow(ClientOp.TABLE, "row1", "a", "1");
		ClientOp.selectRow(TABLE, "row1");
		HTable table = new HTable(conf, TABLE);
		Increment increment = new Increment("row1".getBytes());
		 increment.addColumn("cf".getBytes(), "a".getBytes(), 100);
		 
		System.out.println("====================");
		System.out.println(increment.toString());
		ClientOp.selectRow(TABLE, "row1");
		ClientOp.deleteRow(TABLE, "row1");
	}
}
