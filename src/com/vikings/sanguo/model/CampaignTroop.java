/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-4-23 下午4:21:43
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class CampaignTroop {
	private int id;  //部队方案ID（奇数为我方，偶数为敌方）
	private int troopId;  //士兵ID
	private int troopAmt;  //士兵数量
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setTroopAmt(int troopAmt) {
		this.troopAmt = troopAmt;
	}
	
	public void setTroopId(int troopId) {
		this.troopId = troopId;
	}
	
	public int getId() {
		return id;
	}
	
	public int getTroopAmt() {
		return troopAmt;
	}
	
	public int getTroopId() {
		return troopId;
	}
	
	public static CampaignTroop fromString(String line) {
		StringBuilder buf = new StringBuilder(line);
		CampaignTroop ct = new CampaignTroop();
		
		ct.setId(StringUtil.removeCsvInt(buf));
		ct.setTroopId(StringUtil.removeCsvInt(buf));
		ct.setTroopAmt(StringUtil.removeCsvInt(buf));
		
		return ct;
	}
}
