package com.github.xiaofu.demo.gson;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;



 

import com.github.xiaofu.demo.utils.Tools;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

public class JsonUtilsTest
{
	Gson serializer;

	@Before
	public void setUP()
	{
		serializer =   JsonUtils.getUserSerializer();
		 
	}
	 

	@Test
	public void testSetMonth() throws ParseException
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, 2);
		System.out.println(Tools.formatDate(calendar.getTime(), "yyyy-MM-dd"));
		calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, 1);
		System.out.println(Tools.formatDate(calendar.getTime(), "yyyy-MM-dd"));
		calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, 0);
		System.out.println(Tools.formatDate(calendar.getTime(), "yyyy-MM-dd"));
		calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, -1);
		System.out.println(Tools.formatDate(calendar.getTime(), "yyyy-MM-dd"));
		calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, -2);
		System.out.println(Tools.formatDate(calendar.getTime(), "yyyy-MM-dd"));
		calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, 12);
		System.out.println(Tools.formatDate(calendar.getTime(), "yyyy-MM-dd"));

		// date compareto

		Date beginDate = Tools.parseDate("2014-09-01", "yyyy-MM-dd");
		Date endDate = Tools.parseDate("2014-09-20", "yyyy-MM-dd");
		Date currentDate = Calendar.getInstance().getTime();
		System.out.println(beginDate.compareTo(currentDate));
		System.out.println(endDate.compareTo(currentDate));

	}

}
