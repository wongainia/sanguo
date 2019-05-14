package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class ShopTimeDiscount {
	private byte type;// 1、道具；2、装备
	private int id; // type为1时itemid；2时装备方案号
	private int timeConditionId; // 打折方案号
	private int discount; // 售价百分比

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public void setTimeConditionId(int timeConditionId) {
		this.timeConditionId = timeConditionId;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDiscount() {
		return discount;
	}

	public int getTimeConditionId() {
		return timeConditionId;
	}

	public int getId() {
		return id;
	}

	public static ShopTimeDiscount fromString(String line) {
		StringBuilder buf = new StringBuilder(line);
		ShopTimeDiscount sdd = new ShopTimeDiscount();
		sdd.setType(StringUtil.removeCsvByte(buf));
		sdd.setId(StringUtil.removeCsvInt(buf));
		sdd.setTimeConditionId(StringUtil.removeCsvInt(buf));
		sdd.setDiscount(StringUtil.removeCsvInt(buf));
		return sdd;
	}
}
