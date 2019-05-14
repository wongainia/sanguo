/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-6-7 下午12:08:35
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class FirstRechargeReward {
	private int id;  //id
	private int money;  //充值金额(单位：元宝)
	private int type;  //奖品类型（1:将领，2:物品）
	private int rewardId; //奖品id
	private String rewardDesc;  //奖品描述
	private String rechargeDesc;  //充值奖励描述

	public void setId(int id) {
		this.id = id;
	}
	
	public void setRechargeDesc(String rechargeDesc) {
		this.rechargeDesc = rechargeDesc;
	}
	
	public void setRewardDesc(String rewardDesc) {
		this.rewardDesc = rewardDesc;
	}
	
	public void setRewardId(int rewardId) {
		this.rewardId = rewardId;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public int getId() {
		return id;
	}
	
	public String getRechargeDesc() {
		return rechargeDesc;
	}
	
	public String getRewardDesc() {
		return rewardDesc;
	}
	
	public int getRewardId() {
		return rewardId;
	}
	
	public int getType() {
		return type;
	}
	
	public void setMoney(int money) {
		this.money = money;
	}
	
	public int getMoney() {
		return money;
	}
	
	public static FirstRechargeReward fromString (String line) {
		StringBuilder buf = new StringBuilder(line);
		FirstRechargeReward frr = new FirstRechargeReward();
		
		frr.setId(StringUtil.removeCsvInt(buf));
		frr.setMoney(StringUtil.removeCsvInt(buf) / 100);
		frr.setType(StringUtil.removeCsvInt(buf));
		frr.setRewardId(StringUtil.removeCsvInt(buf));
		frr.setRewardDesc(StringUtil.removeCsv(buf));
		frr.setRechargeDesc(StringUtil.removeCsv(buf));
		
		return frr;
	}
}
