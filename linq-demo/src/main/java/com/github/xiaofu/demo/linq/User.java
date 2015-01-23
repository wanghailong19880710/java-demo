package com.github.xiaofu.demo.linq;

import java.util.Date;

public class User
{
	private int id;
	private Date inject;
	private int value;
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public Date getInject()
	{
		return inject;
	}
	public void setInject(Date inject)
	{
		this.inject = inject;
	}
	public int getValue()
	{
		return value;
	}
	public void setValue(int value)
	{
		this.value = value;
	}
	public User(int id,Date inject,int value)
	{
		this.id=id;
		this.inject=inject;
		this.value=value;
	}
	
}
