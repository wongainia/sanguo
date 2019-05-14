package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class ManorDraftResource {
	private int buildingId; // 建筑
	private int armId; // 兵种
	private int resourceType; // 1资源、2物品
	private int value; // type为1填资源id，type为2填物品id
	private int amount; // 10000个兵消耗物品数量

	public int getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(int buildingId) {
		this.buildingId = buildingId;
	}

	public int getArmId() {
		return armId;
	}

	public void setArmId(int armId) {
		this.armId = armId;
	}

	public int getResourceType() {
		return resourceType;
	}

	public void setResourceType(int resourceType) {
		this.resourceType = resourceType;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public static ManorDraftResource fromString(String csv) {
		ManorDraftResource mdr = new ManorDraftResource();
		StringBuilder buf = new StringBuilder(csv);
		mdr.setBuildingId(StringUtil.removeCsvInt(buf));
		mdr.setArmId(StringUtil.removeCsvInt(buf));
		mdr.setResourceType(StringUtil.removeCsvInt(buf));
		mdr.setValue(StringUtil.removeCsvInt(buf));
		mdr.setAmount(StringUtil.removeCsvInt(buf));
		return mdr;
	}
}
