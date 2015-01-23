package com.github.xiaofu.demo.gson; 

public class TitleInfo implements Cloneable
{
	private String _id;
	// 需要转换器
	private String classes;
	private String gch;
	private short years;
	private byte pagecount;

	public String get_id()
	{
		return _id;
	}

	public void set_id(String _id)
	{
		this._id = _id;
	}

	public String getClasses()
	{
		return classes;
	}

	public void setClasses(String classes)
	{
		this.classes = classes;
	}

	public String getGch()
	{
		return gch;
	}

	public void setGch(String gch)
	{
		this.gch = gch;
	}

	public short getYears()
	{
		return years;
	}

	public void setYears(short years)
	{
		this.years = years;
	}

	public byte getPagecount()
	{
		return pagecount;
	}

	public void setPagecount(byte pagecount)
	{
		this.pagecount = pagecount;
	}



}
