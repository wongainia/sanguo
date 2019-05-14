/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-9-23 上午11:33:56
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import java.text.ParseException;
import java.util.Date;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.StringUtil;

public class DayTimeCondition extends TimeTypeCondition {
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
		
		Date now = DateUtil.getNow().getTime();
		if (now.before(getStartDate()) || now.after(getEndDate()))
			return false;
		
		return true;
	}

	public void fromString(int id, int group, int type, StringBuilder buf) {
		setBaseInfo(id, group, type);
		try {
			setStartDate(DateUtil.db2MinuteFormat3.parse(StringUtil.removeCsv(buf)));
			setEndDate(DateUtil.db2MinuteFormat3.parse(StringUtil.removeCsv(buf)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getCountDownSecond() {
		if (DateUtil.getNow().getTime().before(endDate))
			return (int) ((endDate.getTime() - Config.serverTimeSS())); //System.currentTimeMillis()
		return 0;
	} 
}
