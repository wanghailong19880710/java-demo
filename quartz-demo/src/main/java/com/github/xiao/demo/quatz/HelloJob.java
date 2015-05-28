package com.github.xiao.demo.quatz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class HelloJob implements Job
{

	private String say;
	
	
	public String getSay()
	{
		return say;
	}


	public void setSay(String say)
	{
		this.say = say;
	}


	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException
	{
		 
		 System.out.println(getSay());
		
	}

}
