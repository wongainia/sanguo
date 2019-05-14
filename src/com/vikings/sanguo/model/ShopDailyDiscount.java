package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class ShopDailyDiscount {
	private byte type;// 1、道具；2、装备
	private int groupId; // 组号
	private int id; // 物品id
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

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDiscount() {
		return discount;
	}

	public int getGroupId() {
		return groupId;
	}

	public int getId() {
		return id;
	}

	public static ShopDailyDiscount fromString(String line) {
		StringBuilder buf = new StringBuilder(line);
		ShopDailyDiscount sdd = new ShopDailyDiscount();
		sdd.setType(StringUtil.removeCsvByte(buf));
		sdd.setGroupId(StringUtil.removeCsvInt(buf));
		sdd.setId(StringUtil.removeCsvInt(buf));
		sdd.setDiscount(StringUtil.removeCsvInt(buf));
		return sdd;
	}
}
