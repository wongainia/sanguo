/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-1-24 上午11:49:10
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class HolyTroop {
	public static final int TYPE_ARM = 1;
	public static final int TYPE_HERO = 2;

	private int propId; // 圣城id
	private int troopId; // 兵种ID(SoldierID)
	private int troopCnt; // 兵种数量 (type=2时 此字段为：0：主将 1 ,2：副将)
	private int type;// 类型（1士兵、2将领）

	public int getPropId() {
		return propId;
	}

	public void setPropId(int propId) {
		this.propId = propId;
	}

	public void setTroopCnt(int troopCnt) {
		this.troopCnt = troopCnt;
	}

	public void setTroopId(int troopId) {
		this.troopId = troopId;
	}

	public int getTroopCnt() {
		return troopCnt;
	}

	public int getTroopId() {
		return troopId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	static public HolyTroop fromString(String line) {
		HolyTroop ht = new HolyTroop();
		StringBuilder buf = new StringBuilder(line);
		ht.propId = StringUtil.removeCsvInt(buf);
		ht.troopId = StringUtil.removeCsvInt(buf);
		ht.troopCnt = StringUtil.removeCsvInt(buf);
		ht.type = StringUtil.removeCsvInt(buf);
		return ht;
	}
}
