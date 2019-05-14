/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-10-9 下午2:16:33
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class HonorRank {
	private int typeId;  //荣耀榜类型（1铁血、2家族、3神将）,同HonorRankType
	private int fromPlace;  //名次上限
	private int toPlace;  //名次下限
	private int itemId;  //奖品id
	
	public void setFromPlace(int fromPlace) {
		this.fromPlace = fromPlace;
	}
	
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	
	public void setToPlace(int toPlace) {
		this.toPlace = toPlace;
	}
	
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	
	public int getFromPlace() {
		return fromPlace;
	}
	
	public int getItemId() {
		return itemId;
	}
	
	public int getToPlace() {
		return toPlace;
	}
	
	public int getTypeId() {
		return typeId;
	}
	
	static public HonorRank fromString(String line) {
		StringBuilder buf = new StringBuilder(line);
		
		HonorRank hr = new HonorRank();
		hr.setTypeId(StringUtil.removeCsvInt(buf));
		hr.setFromPlace(StringUtil.removeCsvInt(buf));
		hr.setToPlace(StringUtil.removeCsvInt(buf));
		hr.setItemId(StringUtil.removeCsvInt(buf));
		
		return hr;
	}
}
