package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

/**
 * 装备品质相关配置
 * 
 * @author susong
 * 
 */
public class EquipmentType {
	private int planId;
	private byte quality;
	private int maxGemLevel;// 最高承载宝石等级
	private int userLevel;// 升级下一品质最低玩家等级

	public int getPlanId() {
		return planId;
	}

	public void setPlanId(int planId) {
		this.planId = planId;
	}

	public byte getQuality() {
		return quality;
	}

	public void setQuality(byte quality) {
		this.quality = quality;
	}

	public int getMaxGemLevel() {
		return maxGemLevel;
	}

	public void setMaxGemLevel(int maxGemLevel) {
		this.maxGemLevel = maxGemLevel;
	}

	public int getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(int userLevel) {
		this.userLevel = userLevel;
	}

	public static EquipmentType fromString(String line) {
		EquipmentType et = new EquipmentType();
		StringBuilder buf = new StringBuilder(line);
		et.setPlanId(StringUtil.removeCsvInt(buf));
		et.setQuality(StringUtil.removeCsvByte(buf));
		et.setMaxGemLevel(StringUtil.removeCsvInt(buf));
		et.setUserLevel(StringUtil.removeCsvInt(buf));
		return et;
	}
}
