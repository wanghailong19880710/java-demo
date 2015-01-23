package com.github.xiaofu.demo.gson;

import java.util.Date;

public class InjectDateTable
{
	private Date modify_month;
	private InjectCountsOfDate[] datas;

	public Date getModify_month()
	{
		return modify_month;
	}

	public void setModify_month(Date modify_month)
	{
		this.modify_month = modify_month;
	}

	public InjectCountsOfDate[] getDatas()
	{
		return datas;
	}

	public void setDatas(InjectCountsOfDate[] datas)
	{
		this.datas = datas;
	}
}
