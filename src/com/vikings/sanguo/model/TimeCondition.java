/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-4-23 下午4:40:20
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class TimeCondition {

	private int id; // 方案ID
	private int openTime; // 副本每日开启点（全天开则填0，有则按此规则10:00:00） 单位秒 一天的秒数
	private int closeTime; // 副本每日关闭点（无则填0，有则按此规则10:00:00）
	private int times; // 每次开启时段内限次

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(int closeTime) {
		this.closeTime = closeTime;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public void setOpenTime(int openTime) {
		this.openTime = openTime;
	}

	public int getOpenTime() {
		return openTime;
	}

	public static TimeCondition fromString(String line) {
		StringBuilder buf = new StringBuilder(line);
		TimeCondition tc = new TimeCondition();
		tc.setId(StringUtil.removeCsvInt(buf));
		tc.setOpenTime(StringUtil.parseSS(StringUtil.removeCsv(buf)));
		tc.setCloseTime(StringUtil.parseSS(StringUtil.removeCsv(buf)));
		tc.setTimes(StringUtil.removeCsvInt(buf));
		return tc;
	}
}
