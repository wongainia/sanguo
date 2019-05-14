/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-4-23 下午3:01:05
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 章节奖励信息
 */
package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class CheckInRewards {
	private int vip;
	private int days;
	private int type;
	private int thingId;
	private int amount;

	public void setVip(int vip) {
		this.vip = vip;
	}
	
	public int getVip() {
		return vip;
	}
	
	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getThingId() {
		return thingId;
	}

	public void setThingId(int thingId) {
		this.thingId = thingId;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	static public CheckInRewards fromString(String line) {
		StringBuilder buf = new StringBuilder(line);
		CheckInRewards cr = new CheckInRewards();
		cr.setVip(StringUtil.removeCsvInt(buf));
		cr.setDays(StringUtil.removeCsvInt(buf));
		cr.setType(StringUtil.removeCsvInt(buf));
		cr.setThingId(StringUtil.removeCsvInt(buf));
		cr.setAmount(StringUtil.removeCsvInt(buf));
		return cr;
	}
}
