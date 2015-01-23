/**
 * @ProjectName: inject-water
 * @Copyright: 版权所有 Copyright © 2001-2012 cqvip.com Inc. All rights reserved. 
 * @address: http://www.cqvip.com
 * @date: 2014-9-4 下午4:52:09
 * @Description: 本内容仅限于维普公司内部使用，禁止转发.
 */
package com.github.xiaofu.demo.gson; 

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
 
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * <p>
 * </p>
 * 
 * @author fulaihua 2014-9-4 下午4:52:09
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {flh} 2015-1-13
 * @modify by reason:{getUserSerializer}:{直接返回单例}
 * @modify by reason:{getTitleInfoSerializer}:{直接返回单例}
 */
public final class JsonUtils
{

	private volatile static Gson userSerializer;
	private volatile static Gson titleSerializer;
	private static final Lock LOCK = new ReentrantLock();

	private JsonUtils()
	{
	}
	/**
	 * InjectWaterConfigUser对象的序列化器
	 * @author fulaihua 2015-1-13 下午3:04:53
	 * @return
	 */
	public static Gson getUserSerializer()
	{
		if (userSerializer == null)
		{
			try
			{
				LOCK.lock();
				if (userSerializer == null)
				{
					GsonBuilder builder = new GsonBuilder();
					builder.excludeFieldsWithoutExposeAnnotation();
					builder.registerTypeAdapter(InjectDateTable.class,
							new InjectDateTableAdapter());
					builder.setDateFormat("yyyy-MM-dd");
					userSerializer = builder.create();
				}
			}
			finally
			{
				LOCK.unlock();
			}
		}
		return userSerializer;
	}
	/**
	 * TitleInfo对象的序列化器
	 * @author fulaihua 2015-1-13 下午3:04:48
	 * @return
	 */
	public static Gson getTitleInfoSerializer()
	{
		if (titleSerializer == null)
		{
			try
			{
				LOCK.lock();
				if (titleSerializer == null)
				{
					GsonBuilder builder = new GsonBuilder();
					builder.setFieldNamingStrategy(new FieldNamingStrategy()
					{
						@Override
						public String translateName(Field f)
						{
							if (f.getName().equalsIgnoreCase("classes"))
								return "class";
							return f.getName();
						}
					});
					titleSerializer = builder.create();
				}
			}
			finally
			{
				LOCK.unlock();
			}
		}
		return titleSerializer;
	}
	

	private static class InjectDateTableAdapter implements
			JsonDeserializer<InjectDateTable>, JsonSerializer<InjectDateTable>
	{

		@Override
		public InjectDateTable deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException
		{

			InjectDateTable result = new InjectDateTable();
			String[] yearMonth = json.getAsJsonObject().get("modify_month")
					.getAsString().split("-");
			Calendar currentCalendar = Calendar.getInstance();
			currentCalendar.set(Calendar.YEAR, Integer.valueOf(yearMonth[0]));
			currentCalendar.set(Calendar.MONTH,
					Integer.valueOf(yearMonth[1]) - 1);// 注意减1
			currentCalendar.set(Calendar.DAY_OF_MONTH, 1);
			result.setModify_month(currentCalendar.getTime());
			result.setDatas((InjectCountsOfDate[]) context.deserialize(json
					.getAsJsonObject().get("datas").getAsJsonArray(),
					InjectCountsOfDate[].class));
			return result;

		}

		@Override
		public JsonElement serialize(InjectDateTable src, Type typeOfSrc,
				JsonSerializationContext context)
		{

			JsonObject result = new JsonObject();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
		 
			result.addProperty("modify_month",
					formatter.format(src.getModify_month()));
			result.add("datas", context.serialize(src.getDatas()));
			return result;
		}

	}

	 

}
