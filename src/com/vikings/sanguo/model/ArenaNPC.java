/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-12-9 下午5:36:35
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class ArenaNPC {
	private int lordId;  //领主
	private int type;  //类型（1士兵、2将领）
	private int armId;  //兵种ID或将领方案
	private int armCount;  //兵种数量

	public final static int ARM = 1;
	public final static int HERO = 2;
	
	public void setArmCount(int armCount) {
		this.armCount = armCount;
	}
	
	public void setArmId(int armId) {
		this.armId = armId;
	}
	
	public void setLordId(int lordId) {
		this.lordId = lordId;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public int getArmCount() {
		return armCount;
	}
	
	public int getArmId() {
		return armId;
	}
	
	public int getLordId() {
		return lordId;
	}
	
	public int getType() {
		return type;
	}
	
	public static ArenaNPC fromString(String line) {
		StringBuilder buf = new StringBuilder(line);
		ArenaNPC npc = new ArenaNPC();
		npc.setLordId(StringUtil.removeCsvInt(buf));
		npc.setType(StringUtil.removeCsvInt(buf));
		npc.setArmId(StringUtil.removeCsvInt(buf));
		npc.setArmCount(StringUtil.removeCsvInt(buf));
		return npc;
	}
}
