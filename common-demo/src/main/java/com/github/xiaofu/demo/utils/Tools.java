/**
 * @ProjectName: journal-server
 * @Copyright: 版权所有 Copyright © 2001-2012 cqvip.com Inc. All rights reserved. 
 * @address: http://www.cqvip.com
 * @date: 2014-6-16 上午10:05:38
 * @Description: 本内容仅限于维普公司内部使用，禁止转发.
 */
package com.github.xiaofu.demo.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>
 * </p>
 * 
 * @author fulaihua 2014-6-16 上午10:05:38
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2014-6-16
 * @modify by reason:{方法名}:{原因}
 */
public final class Tools
{
	private volatile static String hostName;

	private Tools()
	{
	}

	/**
	 * 
	 * @author fulaihua 2014-6-16 上午10:06:16
	 * @return
	 */
	public static String getHostName()
	{
		if (hostName != null)
			return hostName;
		InetAddress ia = null;
		try
		{
			ia = InetAddress.getLocalHost();
		}
		catch (UnknownHostException e)
		{

		}
		if (ia == null)
			hostName = "localhost";
		else
			hostName = ia.getHostName();
		return hostName;
	}

	public static void threadWait(int millis)
	{
		try
		{
			Thread.sleep(millis);
		}
		catch (InterruptedException e)
		{

		}
	}

	/**
	 * IP地址字符串转换成10进制
	 * 
	 * @author fulaihua 2014-5-8 上午11:44:40
	 * @param strip
	 * @return
	 */
	public static long ipToLong(String strip)
	{
		if (strip == null || strip.trim().isEmpty())
			return 0;
		long[] ip = new long[4];
		String[] ips = strip.trim().split("\\.");
		ip[0] = Long.parseLong(ips[0]);
		ip[1] = Long.parseLong(ips[1]);
		ip[2] = Long.parseLong(ips[2]);
		ip[3] = Long.parseLong(ips[3]);
		return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
	}

	/**
	 * 格式化IP地址为xxx.xxx.xxx.xxx的格式
	 * 
	 * @author fulaihua 2014-7-25 下午1:39:24
	 * @param ipAddress
	 * @return
	 */
	public static String formatIp(String ipAddress)
	{
		if (ipAddress == null || ipAddress.trim().isEmpty())
			return "";
		StringBuilder ipBuilder = new StringBuilder(10);
		String[] ips = ipAddress.trim().split("\\.");
		for (int i = 0; i < ips.length; i++)
		{
			ips[i] = String.format("%03d", Integer.valueOf(ips[i]));
			ipBuilder.append(ips[i]);
			ipBuilder.append(".");
		}
		return ipBuilder.substring(0, ipBuilder.length() - 1);

	}

	/**
	 * 格式日期对象为日期字符串
	 * 
	 * @author fulaihua 2014-8-11 下午2:19:34
	 * @param date
	 * @param formatStyle
	 * @return if date is null,return  ""
	 */
	public static String formatDate(Date date, String formatStyle)
	{
		if (date == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat(formatStyle);
		return sdf.format(date);
	}

	/**
	 * 解析日期字符串为日期对象
	 * 
	 * @author fulaihua 2014-8-11 下午2:19:11
	 * @param strDate
	 * @param formatStyle
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String strDate, String formatStyle)
			throws ParseException
	{
		if (strDate == null || strDate.isEmpty())
			return Calendar.getInstance().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat(formatStyle);
		return sdf.parse(strDate);
	}
}
