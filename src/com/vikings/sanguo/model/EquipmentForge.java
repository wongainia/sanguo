package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class EquipmentForge {
	private int scheme; // 锻造方案号
	private byte level; // 锻造技能等级
	private int itemId;// 消耗道具id
	private int count;// 道具数量
	private int price;// 单个道具折算元宝价格
	private int max;// 本级锻造值上限
	private int minQuality;// 锻造到下一级需要的最低品质要求

	public int getScheme() {
		return scheme;
	}

	public void setScheme(int scheme) {
		this.scheme = scheme;
	}

	public byte getLevel() {
		return level;
	}

	public void setLevel(byte level) {
		this.level = level;
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

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getMinQuality() {
		return minQuality;
	}

	public void setMinQuality(int minQuality) {
		this.minQuality = minQuality;
	}

	public static EquipmentForge fromString(String line) {
		EquipmentForge ef = new EquipmentForge();
		StringBuilder buf = new StringBuilder(line);
		ef.setScheme(StringUtil.removeCsvInt(buf));
		ef.setLevel(StringUtil.removeCsvByte(buf));
		ef.setItemId(StringUtil.removeCsvInt(buf));
		ef.setCount(StringUtil.removeCsvInt(buf));
		ef.setPrice(StringUtil.removeCsvInt(buf));
		ef.setMax(StringUtil.removeCsvInt(buf));
		StringUtil.removeCsv(buf);
		ef.setMinQuality(StringUtil.removeCsvInt(buf));
		return ef;
	}
}
