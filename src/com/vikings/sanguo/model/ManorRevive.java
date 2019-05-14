/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-12-17 上午11:48:45
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class ManorRevive {
	private int minLvl;
	private int maxLvl;
	private int armPropId;
	private int cd;  //秒
	private int rmb;  //10000兵的元宝价格
	private int gold;	//10000兵的金币价格
	private int secondGold;//金币增幅（每复活一次的金币增幅，仅用于金币）
	
	public void setArmPropId(int armPropId) {
		this.armPropId = armPropId;
	}
	
	public void setCd(int cd) {
		this.cd = cd;
	}
	
	public void setMaxLvl(int maxLvl) {
		this.maxLvl = maxLvl;
	}
	
	public void setMinLvl(int minLvl) {
		this.minLvl = minLvl;
	}
	
	public void setRmb(int rmb) {
		this.rmb = rmb;
	}
	
	public void setGold(int gold) {
		this.gold = gold;
	}
	
	public void setSecondGold(int secondGold) {
		this.secondGold = secondGold;
	}
	
	public int getArmPropId() {
		return armPropId;
	}
	
	public int getCd() {
		return cd;
	}
	
	public int getMaxLvl() {
		return maxLvl;
	}
	
	public int getMinLvl() {
		return minLvl;
	}
	
	public int getRmb() {
		return rmb;
	}
	
	public int getGold() {
		return gold;
	}
	
	public int getSecondGold() {
		return secondGold;
	}

	

	public static ManorRevive fromString(String line) {
		StringBuilder buf = new StringBuilder(line);
		ManorRevive mr = new ManorRevive();
		mr.setMinLvl(StringUtil.removeCsvInt(buf));
		mr.setMaxLvl(StringUtil.removeCsvInt(buf));
		mr.setArmPropId(StringUtil.removeCsvInt(buf));
		mr.setCd(StringUtil.removeCsvInt(buf));
		mr.setRmb(StringUtil.removeCsvInt(buf));
		mr.setGold(StringUtil.removeCsvInt(buf));
		mr.setSecondGold(StringUtil.removeCsvInt(buf));
		return mr;
	}
}
