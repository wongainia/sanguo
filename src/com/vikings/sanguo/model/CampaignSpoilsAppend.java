/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-6-21 上午11:55:14
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class CampaignSpoilsAppend {
	private int id;  //战役ID
	private int type;  //奖励类型（1：固定奖励，2：战绩奖励，3：意外奖励）
	private int spoilType;  //奖品type（0为无消耗，1、属性 2、物品 3、建筑要求 4、将领 5、士兵）
	private int spoilId;  //奖品ID（属性定义见属性id，物品填对应物品ID）
	private int count;  //数量

	public void setCount(int count) {
		this.count = count;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setSpoilId(int spoilId) {
		this.spoilId = spoilId;
	}
	
	public void setSpoilType(int spoilType) {
		this.spoilType = spoilType;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public int getCount() {
		return count;
	}
	
	public int getId() {
		return id;
	}
	
	public int getSpoilId() {
		return spoilId;
	}
	
	public int getSpoilType() {
		return spoilType;
	}
	
	public int getType() {
		return type;
	}
	
	public static CampaignSpoilsAppend fromString(String line) {
		StringBuilder buf = new StringBuilder(line);
		CampaignSpoilsAppend csa = new CampaignSpoilsAppend();
		
		csa.id = StringUtil.removeCsvInt(buf);
		csa.type = StringUtil.removeCsvInt(buf);
		csa.spoilType = StringUtil.removeCsvInt(buf);
		csa.spoilId = StringUtil.removeCsvInt(buf);
		csa.count = StringUtil.removeCsvInt(buf);
		
		return csa;
	}
}
