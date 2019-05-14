package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class SiteSpecial {
	private int fiefPropId; // 地形id（对应prop_fief的propid）
	private int buildingPropId; // 资源建筑id
	private int itemId; // 特殊产出物id
	private int count; // 特殊产出物数量
	private int value; // 1个特殊产物对应资源的价值
	private int weight; // 1个特殊产物重量
	private String desc;// 客户端显示内容（不配显示默认）

	public int getFiefPropId() {
		return fiefPropId;
	}

	public void setFiefPropId(int fiefPropId) {
		this.fiefPropId = fiefPropId;
	}

	public int getBuildingPropId() {
		return buildingPropId;
	}

	public void setBuildingPropId(int buildingPropId) {
		this.buildingPropId = buildingPropId;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public static SiteSpecial fromString(String line) {
		SiteSpecial siteSpecial = new SiteSpecial();
		StringBuilder buf = new StringBuilder(line);
		siteSpecial.setFiefPropId(StringUtil.removeCsvInt(buf));
		siteSpecial.setBuildingPropId(StringUtil.removeCsvInt(buf));
		siteSpecial.setItemId(StringUtil.removeCsvInt(buf));
		siteSpecial.setCount(StringUtil.removeCsvInt(buf));
		siteSpecial.setValue(StringUtil.removeCsvInt(buf));
		siteSpecial.setWeight(StringUtil.removeCsvInt(buf));
		siteSpecial.setDesc(StringUtil.removeCsv(buf));
		return siteSpecial;
	}

}
