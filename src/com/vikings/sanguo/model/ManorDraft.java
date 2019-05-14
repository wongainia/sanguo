package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

/**
 * 庄园征兵
 * 
 * @author susong
 * 
 */
public class ManorDraft {
	private int buildingId; // 建筑
	private int armId; // 兵种
	private int cd; // cd时间 ，单位秒
	private int resourceDraftRate; // 资源招募百分比
	private int cost; // 消耗元宝(10000兵的元宝价格)

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

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getCd() {
		return cd;
	}

	public void setCd(int cd) {
		this.cd = cd;
	}

	public int getResourceDraftRate() {
		return resourceDraftRate;
	}

	public void setResourceDraftRate(int resourceDraftRate) {
		this.resourceDraftRate = resourceDraftRate;
	}

	public static ManorDraft fromString(String csv) {
		ManorDraft draft = new ManorDraft();
		StringBuilder buf = new StringBuilder(csv);
		draft.setBuildingId(StringUtil.removeCsvInt(buf));
		draft.setArmId(StringUtil.removeCsvInt(buf));
		draft.setCd(StringUtil.removeCsvInt(buf));
		draft.setCost(StringUtil.removeCsvInt(buf));
		draft.setResourceDraftRate(StringUtil.removeCsvInt(buf));
		return draft;
	}
}
