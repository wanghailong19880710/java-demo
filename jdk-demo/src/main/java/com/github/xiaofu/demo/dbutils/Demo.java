/**
 * 
 */
package com.github.xiaofu.demo.dbutils;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

/**
 * @author home
 *
 */
public class Demo {

	 static String QUERY_SQL="select * from test";
	/**
	 * 字段名大小和属性名大小是没有问题的，DBUTILS会进行忽略大小的比较
	 * @param args
	 * @throws SQLException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public static void main(String[] args) throws SQLException, IllegalAccessException, InvocationTargetException {
		
		QueryRunner runner=new QueryRunner(DBCPUtils.getDataSource());
		List<TestModel> lists= runner.query(QUERY_SQL, new BeanListHandler<TestModel>(TestModel.class));
		System.out.println(lists.size());
		TestModel destModel=new TestModel();
		for (TestModel model : lists) {
			BeanUtils.copyProperties(destModel, model);
			System.out.println(model.getT3());
		}
	}

}
