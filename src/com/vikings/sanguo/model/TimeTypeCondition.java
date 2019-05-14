/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-9-16 下午4:35:21
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public abstract class TimeTypeCondition {
	protected int id;              //方案id
	protected int group;           //分组
	protected int type;            //type(1.自然天 2.星期 3.小时 4.注册时间计算/秒)
	
	public void setGroup(int group) {
		this.group = group;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public int getGroup() {
		return group;
	}
	
	public int getId() {
		return id;
	}
	
	public int getType() {
		return type;
	}
	
	public abstract boolean isWithinTime(); 
	
	public abstract int getCountDownSecond();
	
	protected void setBaseInfo(int id, int group, int type) {
		setId(id);
		setGroup(group);
		setType(type);
	}
	
	public static TimeTypeCondition fromString(String line) {
		StringBuilder buf = new StringBuilder(line);
		
		int id = StringUtil.removeCsvInt(buf);
		int group = StringUtil.removeCsvInt(buf);
		int type = StringUtil.removeCsvInt(buf);

		switch (type) {
		case 1:
			DayTimeCondition dtc = new DayTimeCondition();
			dtc.fromString(id, group, type, buf);
			return dtc;
		case 2:
			WeekTimeCondition wtc = new WeekTimeCondition();
			wtc.fromString(id, group, type, buf);
			return wtc;
		case 3:
			HourTimeCondition htc = new HourTimeCondition();
			htc.fromString(id, group, type, buf);
			return htc;
		case 4:
			RegTimeCondition rtc = new RegTimeCondition();
			rtc.fromString(id, group, type, buf);
			return rtc;
		default:
			RegTimeCondition nullRtc = new RegTimeCondition();
			nullRtc.setType(type);
			return nullRtc;
		}
		
	} 
}
