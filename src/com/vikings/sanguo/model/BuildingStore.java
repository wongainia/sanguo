/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-5-24 下午4:16:03
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class BuildingStore {
	private int buildingId;
	private int resId;
	private int maxCount;
	
	public void setBuildingId(int buildingId) {
		this.buildingId = buildingId;
	}
	
	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}
	
	public void setResId(int resId) {
		this.resId = resId;
	}
	
	public int getBuildingId() {
		return buildingId;
	}
	
	public int getMaxCount() {
		return maxCount;
	}
	
	public int getResId() {
		return resId;
	}
	
	public static BuildingStore fromString(String line) {
		StringBuilder buf = new StringBuilder(line);
		BuildingStore bs = new BuildingStore();
		
		bs.buildingId = StringUtil.removeCsvInt(buf);
		bs.resId = StringUtil.removeCsvInt(buf);
		bs.maxCount = StringUtil.removeCsvInt(buf);
		
		return bs;
	}
}
