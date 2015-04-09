package com.github.xiaofu.demo;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

public class Test {
	public static void main(String[] args) throws JsonProcessingException, IOException {
		jsonTest();
//		System.out.println(MDCodingHour("www.cqvip.com"));
//		System.out.println(newMDCodingHour("www.cqvip.com"));
	}

	public static String newMDCodingHour(String tmp) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		String result = "";
		try {
			byte[] strTemp = tmp.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			result = new String(str);
		} catch (Exception e) {

		}
		return result;

	}

	public static String MDCodingHour(String tmp) {
		int ii = 1;
		Date datest = new Date();
		long a = datest.getTime();
		a /= 0x36ee80L;
		String datestr = Long.toString(a);

		String datestr1 = datestr + tmp;
		String jm = "";
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte strTemp[] = datestr1.getBytes();

			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte md[] = mdTemp.digest();

			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}

			jm = new String(str);
		} catch (Exception e) {
			System.out.print("md5校验错误");
			return "error";
		}
		return jm;
	}

	public static void jsonTest() throws JsonProcessingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		String data="[{\"key\":\"134,title_info\",\"value\":\"23,43,423\"},{\"key\":\"222,writer_info\",\"value\":\"111,33,44\"}]";
		ArrayNode rootNode = (ArrayNode) mapper.readTree(data);
		for (JsonNode node : rootNode)
		{
				ObjectNode objNode=(ObjectNode)node;
				System.out.println(objNode.get("key").getTextValue());
				System.out.println(objNode.get("value").getTextValue());
				
		}
	}
}
