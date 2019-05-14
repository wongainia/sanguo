package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class HeroEnhance {
	public static final byte TYPE_BY_NORMAL = 1;
	public static final byte TYPE_BY_CURRENCY = 2;
	public static final byte TYPE_BY_ITEM = 3;
	public static final byte TYPE_BY_ONE_KEY = 4;
	private byte type;// 强化方式(1常规强化 2元宝强化 99元宝突破——道具强化、一键强化无需配置)
	private int cost;// 消耗值
	private int above;// 当前下限
	private int below;// 当前上限
	private int minAdd;// 增加下限
	private int maxAdd;// 增加上限
	private int costType;// 消耗类型（3功勋，20金币，7元宝）

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getAbove() {
		return above;
	}

	public void setAbove(int above) {
		this.above = above;
	}

	public int getBelow() {
		return below;
	}

	public void setBelow(int below) {
		this.below = below;
	}

	public int getMinAdd() {
		return minAdd;
	}

	public void setMinAdd(int minAdd) {
		this.minAdd = minAdd;
	}

	public int getMaxAdd() {
		return maxAdd;
	}

	public void setMaxAdd(int maxAdd) {
		this.maxAdd = maxAdd;
	}

	public int getCostType() {
		return costType;
	}

	public void setCostType(int costType) {
		this.costType = costType;
	}

	public String getName() {
		String name = "强化";
		switch (type) {
		case TYPE_BY_NORMAL:
			name = ReturnInfoClient.getAttrTypeName(costType) + "强化";
			break;
		case TYPE_BY_CURRENCY:
			name = "元宝强化";
			break;
		case TYPE_BY_ITEM:
			name = "道具强化 ";
			break;
		case TYPE_BY_ONE_KEY:
			name = "至尊强化";
			break;
		default:
			break;
		}
		return name;
	}

	public static HeroEnhance fromString(String csv) {
		HeroEnhance heroEnhance = new HeroEnhance();
		StringBuilder buf = new StringBuilder(csv);
		heroEnhance.setType(StringUtil.removeCsvByte(buf));
		heroEnhance.setCost(StringUtil.removeCsvInt(buf));
		heroEnhance.setAbove(StringUtil.removeCsvInt(buf));
		heroEnhance.setBelow(StringUtil.removeCsvInt(buf));
		heroEnhance.setMinAdd(StringUtil.removeCsvInt(buf));
		heroEnhance.setMaxAdd(StringUtil.removeCsvInt(buf));
		heroEnhance.setCostType(StringUtil.removeCsvInt(buf));
		return heroEnhance;
	}
}
