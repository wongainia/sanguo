/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-9-23 上午11:54:23
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.utils.StringUtil;

public class RegTimeCondition extends TimeTypeCondition{
	private int startSec;    //开始时间,单位秒
	private int endSec;      //结束时间,单位秒

	public void setEndSec(int endSec) {
		this.endSec = endSec;
	}
	
	public void setStartSec(int startSec) {
		this.startSec = startSec;
	}
	
	public int getEndSec() {
		return endSec;
	}
	
	public int getStartSec() {
		return startSec;
	}
	
	@Override
	public boolean isWithinTime() {
		int now = Config.serverTimeSS(); //(int) (System.currentTimeMillis() / 1000);
		if (now < Account.user.getRegTime() + startSec
				|| now >= Account.user.getRegTime() + endSec)
			return false;

		return true;
	}

	public void fromString(int id, int group, int type, StringBuilder buf) {
		setBaseInfo(id, group, type);
		setStartSec(StringUtil.removeCsvInt(buf));
		setEndSec(StringUtil.removeCsvInt(buf));
	}

	@Override
	public int getCountDownSecond() {
		int countDown = (int) (Account.user.getRegTime() + endSec - Config
				.serverTime() / 1000);
		return countDown > 0 ? countDown : 0;
	}
}
