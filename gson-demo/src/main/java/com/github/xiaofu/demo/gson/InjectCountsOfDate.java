/**
 * @ProjectName: inject-water
 * @Copyright: 版权所有 Copyright © 2001-2012 cqvip.com Inc. All rights reserved. 
 * @address: http://www.cqvip.com
 * @date: 2014-9-4 下午3:16:21
 * @Description: 本内容仅限于维普公司内部使用，禁止转发.
 */
package com.github.xiaofu.demo.gson;

import java.util.Date;

/**
 * <p></p>
 * @author fulaihua 2014-9-4 下午3:16:21
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2014-9-4
 * @modify by reason:{方法名}:{原因}
 */
public class InjectCountsOfDate
{
	private Date modify_date;
	private int modfiy_value;

	public Date getModify_date()
	{
		return modify_date;
	}

	public void setModify_date(Date modify_date)
	{
		this.modify_date = modify_date;
	}

	public int getModfiy_value()
	{
		return modfiy_value;
	}

	public void setModfiy_value(int modfiy_value)
	{
		this.modfiy_value = modfiy_value;
	}
}
