/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-9-23 上午11:52:05
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import java.text.ParseException;
import java.util.Date;


import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.StringUtil;

public class HourTimeCondition extends TimeTypeCondition{
	private Date startDate;
	private Date endDate;

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	@Override
	public boolean isWithinTime() {
		if (null == getStartDate() || null == getEndDate())
			return false;

		Date curDate = DateUtil.getTime2Now();
		if (null == curDate)
			return false;

		if (curDate.before(getStartDate()) || curDate.after(getEndDate()))
			return false;

		return true;
	}

	public void fromString(int id, int group, int type, StringBuilder buf) {
		setBaseInfo(id, group, type);
		try {
			setStartDate(DateUtil.time2.parse(StringUtil.removeCsv(buf).trim()));
			setEndDate(DateUtil.time2.parse(StringUtil.removeCsv(buf).trim()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getCountDownSecond() {
		Date curDate = DateUtil.getTime2Now();
		if (null == curDate)
			return 0;
		
		if (curDate.before(endDate))
			return (int) ((endDate.getTime() - curDate.getTime()) / 1000);
		return 0;
	} 
}
