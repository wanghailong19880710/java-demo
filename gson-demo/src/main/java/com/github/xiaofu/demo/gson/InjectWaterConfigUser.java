package com.github.xiaofu.demo.gson;

import java.util.Date;
import com.google.gson.annotations.*;
public class InjectWaterConfigUser
{
	@Expose
	private String guid;
	@Expose
	private int user_id;
	@Expose
	private byte site_id;
	@Expose
	private byte site_type;
	@Expose
	private byte inject_position;
	@Expose
	private String classes;
	@Expose
	private int threshold;
	@Expose
	private Date begin_date;
	@Expose
	private Date end_date;
	@Expose
	private boolean continue_inject;
	@Expose
	private double ratio;
	@Expose
	private byte inject_type;
	@Expose
	private boolean igore_threshold;
	@Expose
	private InjectDateTable[] date_table;

	public String getGuid()
	{
		return guid;
	}

	public void setGuid(String guid)
	{
		this.guid = guid;
	}

	public int getUser_id()
	{
		return user_id;
	}

	public void setUser_id(int user_id)
	{
		this.user_id = user_id;
	}

	public byte getSite_id()
	{
		return site_id;
	}

	public void setSite_id(byte site_id)
	{
		this.site_id = site_id;
	}

	public byte getSite_type()
	{
		return site_type;
	}

	public void setSite_type(byte site_type)
	{
		this.site_type = site_type;
	}

	public byte getInject_position()
	{
		return inject_position;
	}

	public void setInject_position(byte inject_position)
	{
		this.inject_position = inject_position;
	}

	public String getClasses()
	{
		return classes;
	}

	public void setClasses(String classes)
	{
		this.classes = classes;
	}

	public int getThreshold()
	{
		return threshold;
	}

	public void setThreshold(int threshold)
	{
		this.threshold = threshold;
	}

	public Date getBegin_date()
	{
		return begin_date;
	}

	public void setBegin_date(Date begin_date)
	{
		this.begin_date = begin_date;
	}

	public Date getEnd_date()
	{
		return end_date;
	}

	public void setEnd_date(Date end_date)
	{
		this.end_date = end_date;
	}

	public boolean isContinue_inject()
	{
		return continue_inject;
	}

	public void setContinue_inject(boolean continue_inject)
	{
		this.continue_inject = continue_inject;
	}

	public double getRatio()
	{
		return ratio;
	}

	public void setRatio(double ratio)
	{
		this.ratio = ratio;
	}

	public byte getInject_type()
	{
		return inject_type;
	}

	public void setInject_type(byte inject_type)
	{
		this.inject_type = inject_type;
	}

	public boolean isIgore_threshold()
	{
		return igore_threshold;
	}

	public void setIgore_threshold(boolean igore_threshold)
	{
		this.igore_threshold = igore_threshold;
	}

	public InjectDateTable[] getDate_table()
	{
		return date_table;
	}

	public void setDate_table(InjectDateTable[] date_table)
	{
		this.date_table = date_table;
	}
 

}
