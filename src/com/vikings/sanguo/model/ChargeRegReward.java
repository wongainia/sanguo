/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-9-16 下午4:28:54
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class ChargeRegReward {
	private int startSec;              //起始时间（注册时间起算）,单位秒
	private int endSec;                //结束时间（注册时间起算）
	private int min;                   //单笔最小充值
	private int max;                   //单笔最大充值
	private int percent;               //赠送百分比

	public void setEndSec(int endSec) {
		this.endSec = endSec;
	}
	
	public void setMax(int max) {
		this.max = max;
	}
	
	public void setMin(int min) {
		this.min = min;
	}
	
	public void setPercent(int percent) {
		this.percent = percent;
	}
	
	public void setStartSec(int startSec) {
		this.startSec = startSec;
	}
	
	public int getEndSec() {
		return endSec;
	}
	
	public int getMax() {
		return max;
	}
	
	public int getMin() {
		return min;
	}
	
	public int getPercent() {
		return percent;
	}
	
	public int getStartSec() {
		return startSec;
	}
	
	public static ChargeRegReward fromString(String line) {
		StringBuilder buf = new StringBuilder(line);
		ChargeRegReward crr = new ChargeRegReward();
		crr.setStartSec(StringUtil.removeCsvInt(buf));
		crr.setEndSec(StringUtil.removeCsvInt(buf));
		crr.setMin(StringUtil.removeCsvInt(buf));
		crr.setMax(StringUtil.removeCsvInt(buf));
		crr.setPercent(StringUtil.removeCsvInt(buf));
		
		return crr;
	}
}
