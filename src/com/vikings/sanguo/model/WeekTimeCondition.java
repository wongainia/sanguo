/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-9-23 上午11:49:20
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import java.util.Calendar;

import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.StringUtil;

public class WeekTimeCondition extends TimeTypeCondition{
	private int startDayOfWeek;    //开始时间
	private int endDayOfWeek;      //结束时间

	public void setEndDayOfWeek(int endDayOfWeek) {
		this.endDayOfWeek = endDayOfWeek;
	}
	
	public void setStartDayOfWeek(int startDayOfWeek) {
		this.startDayOfWeek = startDayOfWeek;
	}
	
	public int getStartDayOfWeek() {
		return startDayOfWeek;
	}
	
	public int getEndDayOfWeek() {
		return endDayOfWeek;
	}
	
	@Override
	public boolean isWithinTime() {
		int dayOfWeek = DateUtil.getNow().get(Calendar.DAY_OF_WEEK);
		
		if (getStartDayOfWeek() <= getEndDayOfWeek()) {
			if (dayOfWeek < getStartDayOfWeek() || dayOfWeek > getEndDayOfWeek())
				return false;
		} else{
			if (!((dayOfWeek >= getStartDayOfWeek() && dayOfWeek <= 7)
					|| (dayOfWeek >= 0 && dayOfWeek <= getEndDayOfWeek())))
				return false;
		}
		
		return true;
	}

	public void fromString(int id, int group, int type, StringBuilder buf) {
		setBaseInfo(id, group, type);
		setStartDayOfWeek(StringUtil.removeCsvInt(buf) + 1);
		setEndDayOfWeek(StringUtil.removeCsvInt(buf) + 1);
	}

	@Override
	public int getCountDownSecond() {
		return 0;
	} 
}
