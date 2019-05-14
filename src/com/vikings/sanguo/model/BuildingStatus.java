package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class BuildingStatus {

	private int buildingId;
	/*
	 * 20金币产出速度; 21 粮草产出速度 ;22木材产出速度; 23 铁产出速度; 24 铜产出速度;
	 * 
	 * 501 //人口增长速度
	 */
	private int statusId;
	private int value;

	public int getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(int buildingId) {
		this.buildingId = buildingId;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public static BuildingStatus fromString(String cvs) {
		BuildingStatus buildingStatus = new BuildingStatus();
		StringBuilder buf = new StringBuilder(cvs);
		buildingStatus.setBuildingId(StringUtil.removeCsvInt(buf));
		buildingStatus.setStatusId(StringUtil.removeCsvInt(buf));
		buildingStatus.setValue(StringUtil.removeCsvInt(buf));
		return buildingStatus;
	}
}
