package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class PropRouletteItem {
	private int itemId;// 可馈赠的物品ID
	private int good;// 增加好感值（1=0.1%）

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getGood() {
		return good;
	}

	public void setGood(int good) {
		this.good = good;
	}

	public float getGoodPercent() {
		return good / 10f;
	}

	public static PropRouletteItem fromString(String line) {
		PropRouletteItem prop = new PropRouletteItem();
		StringBuilder buf = new StringBuilder(line);
		prop.setItemId(StringUtil.removeCsvInt(buf));
		prop.setGood(StringUtil.removeCsvInt(buf));
		return prop;
	}
}
