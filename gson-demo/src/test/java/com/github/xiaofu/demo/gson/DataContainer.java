/**
 * @ProjectName: inject-water
 * @Copyright: 版权所有 Copyright © 2001-2012 cqvip.com Inc. All rights reserved. 
 * @address: http://www.cqvip.com
 * @date: 2014-9-4 下午2:42:53
 * @Description: 本内容仅限于维普公司内部使用，禁止转发.
 */
package com.github.xiaofu.demo.gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * <p></p>
 * @author fulaihua 2014-9-4 下午2:42:53
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2014-9-4
 * @modify by reason:{方法名}:{原因}
 */
public final class DataContainer
{
	 
	public static JsonArray getData()
	{
		SimpleDateFormat formatter = new SimpleDateFormat();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		builder.setPrettyPrinting();
		Gson json = builder.create();
		JsonObject obj = new JsonObject();
		obj.addProperty("guid", UUID.randomUUID().toString());
		obj.addProperty("user_id", 24324);
		obj.addProperty("site_Id", 3);
		obj.addProperty("site_type", 0);
		obj.addProperty("inject_position", 1);
		obj.addProperty("classes", "ZZ");
		obj.addProperty("threshold", 1000);
		obj.addProperty("begin_date", "2014-08-01");
		obj.addProperty("end_date", "2014-08-31");
		obj.addProperty("continue_inject", false);
		obj.addProperty("ratio", 2.2);
		obj.addProperty("inject_type", 0);

		Calendar calendar = new GregorianCalendar();
		int day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		JsonArray date_table = new JsonArray();
		JsonObject month = new JsonObject();
		JsonArray datas = new JsonArray();
		formatter.applyPattern("yyyy-MM");
		month.addProperty("modify_month",
				formatter.format(Calendar.getInstance().getTime()));
		formatter.applyPattern("yyyy-MM-dd");
		for (int i = 1; i <= day; i++)
		{
			Calendar days = Calendar.getInstance();
			days.set(days.get(Calendar.YEAR), days.get(Calendar.MONTH), i);
			JsonObject model = new JsonObject();
			model.addProperty("modify_date", formatter.format(days.getTime()));
			model.addProperty("modfiy_value", 1000);
			datas.add(model);
		}
		month.add("datas", datas);
		date_table.add(month);
		obj.add("date_table", date_table);
		JsonArray result = new JsonArray();
		result.add(obj);
		return result;
	}
}
