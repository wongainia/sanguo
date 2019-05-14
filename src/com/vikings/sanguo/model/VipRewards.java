package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class VipRewards {
	public static final byte TYPE_ITEM = 2;
	public static final byte TYPE_HERO = 4;
	public static final byte TYPE_ARM = 5;
	public static final byte TYPE_EQUIP = 7;

	/**
	 * 奖励类型 奖励ID * 2物品 物品ID * 4将领 将领方案ID * 5士兵 士兵ID * 7装备 装备ID
	 */
	private byte vipLevel;
	private byte type;
	private int id;
	private int count;

	public byte getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(byte vipLevel) {
		this.vipLevel = vipLevel;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public static VipRewards fromString(String line) {
		VipRewards vr = new VipRewards();
		StringBuilder buf = new StringBuilder(line);
		vr.setVipLevel(StringUtil.removeCsvByte(buf));
		vr.setType(StringUtil.removeCsvByte(buf));
		vr.setId(StringUtil.removeCsvInt(buf));
		vr.setCount(StringUtil.removeCsvInt(buf));
		return vr;
	}

}
