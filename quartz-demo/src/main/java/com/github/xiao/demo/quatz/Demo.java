/**
 * @ProjectName: quartz-demo
 * @Copyright: 版权所有 Copyright © 2001-2012 cqvip.com Inc. All rights reserved. 
 * @address: http://www.cqvip.com
 * @date: 2015年5月25日 下午5:08:51
 * @Description: 本内容仅限于维普公司内部使用，禁止转发.
 */
package com.github.xiao.demo.quatz;

import org.quartz.DateBuilder;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 * <p>
 * </p>
 * 
 * @author flh 2015年5月25日 下午5:08:51
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2015年5月25日
 * @modify by reason:{方法名}:{原因}
 */
public class Demo
{

	/**
	 * @author flh 2015年5月25日 下午5:08:51
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			// Grab the Scheduler instance from the Factory
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

			// and start it off
			scheduler.start();
			JobDetail job = JobBuilder.newJob(HelloJob.class)
					.withIdentity("myJob", "group1").usingJobData("say", "jb")
					.build();

			// Trigger the job to run now, and then every 40 seconds
			Trigger trigger = TriggerBuilder
					.newTrigger()
					.withIdentity("myTrigger", "group1")
					.withSchedule(
							SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(2)
									.withRepeatCount(10))
					.startAt(DateBuilder.futureDate(10, IntervalUnit.SECOND))
					.build();

			// Tell quartz to schedule the job using our trigger
			scheduler.scheduleJob(job, trigger);
			Trigger trigger1 = TriggerBuilder
					.newTrigger()
					.withIdentity("myTrigger1", "group1")
					.startAt(DateBuilder.futureDate(1, IntervalUnit.SECOND))
					.forJob("myJob","group1")
					.build();
			// scheduler.shutdown();
			scheduler.scheduleJob(trigger1);
			 
		}
		catch (SchedulerException se)
		{
			se.printStackTrace();
		}

	}

}
