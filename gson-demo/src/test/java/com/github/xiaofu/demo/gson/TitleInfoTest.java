package com.github.xiaofu.demo.gson; 

import static org.junit.Assert.assertEquals;

import org.junit.Test;


import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class TitleInfoTest
{

	@Test
	public void testDeserializer()
	{
		JsonObject obj = new JsonObject();
		obj.addProperty("_id", "342");
		obj.addProperty("class", "TP323");
		obj.addProperty("gch", "53253X");
		obj.addProperty("years", "324");
		obj.addProperty("pagecount", "");
		Gson serializer =  JsonUtils.getTitleInfoSerializer();
		 
		TitleInfo model = serializer.fromJson(obj, TitleInfo.class);
		assertEquals("342", model.get_id());
		assertEquals("TP323", model.getClasses());
		assertEquals("53253X", model.getGch());
		assertEquals(324, model.getYears());

	}
}
