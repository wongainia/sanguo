/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-6-20 下午8:42:15
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import java.util.List;

public class EnemyArmInfo {
	private int type;  //1:必选boss 2:可选boss 3：敌军部队
	private String desc;
	private List<ArmInfoClient> troop;
	
	public EnemyArmInfo(int type) {
		this.type = type;
	}
	
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public void setTroop(List<ArmInfoClient> troop) {
		this.troop = troop;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public List<ArmInfoClient> getTroop() {
		return troop;
	}
	
	public int getType() {
		return type;
	}
}
