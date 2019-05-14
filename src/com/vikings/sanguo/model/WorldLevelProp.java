package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class WorldLevelProp {
	private byte level; // 世界等级
	private byte maxHeroTalent; // 将领品质上限
	private byte maxEquipmentQuality;// 装备品质上限
	private String desc;// 开放功能描述

	public byte getLevel() {
		return level;
	}

	public void setLevel(byte level) {
		this.level = level;
	}

	public byte getMaxHeroTalent() {
		return maxHeroTalent;
	}

	public void setMaxHeroTalent(byte maxHeroTalent) {
		this.maxHeroTalent = maxHeroTalent;
	}

	public byte getMaxEquipmentQuality() {
		return maxEquipmentQuality;
	}

	public void setMaxEquipmentQuality(byte maxEquipmentQuality) {
		this.maxEquipmentQuality = maxEquipmentQuality;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public static WorldLevelProp fromString(String line) {
		WorldLevelProp prop = new WorldLevelProp();
		StringBuilder buf = new StringBuilder(line);
		prop.setLevel(StringUtil.removeCsvByte(buf));
		prop.setMaxHeroTalent(StringUtil.removeCsvByte(buf));
		prop.setMaxEquipmentQuality(StringUtil.removeCsvByte(buf));
		StringUtil.removeCsv(buf);
		StringUtil.removeCsv(buf);
		StringUtil.removeCsv(buf);
		StringUtil.removeCsv(buf);
		StringUtil.removeCsv(buf);
		prop.setDesc(StringUtil.removeCsv(buf));
		return prop;
	}
}
