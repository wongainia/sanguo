package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class HeroLevelUp {
	private byte talent;// 品质
	private byte star;// 星级
	private byte level; // LV
	private int needExp;// 升至下一级所需经验

	public byte getTalent() {
		return talent;
	}

	public void setTalent(byte talent) {
		this.talent = talent;
	}

	public byte getStar() {
		return star;
	}

	public void setStar(byte star) {
		this.star = star;
	}

	public byte getLevel() {
		return level;
	}

	public void setLevel(byte level) {
		this.level = level;
	}

	public int getNeedExp() {
		return needExp;
	}

	public void setNeedExp(int needExp) {
		this.needExp = needExp;
	}

	public static HeroLevelUp fromString(String csv) {
		HeroLevelUp heroLevelUp = new HeroLevelUp();
		StringBuilder buf = new StringBuilder(csv);
		heroLevelUp.setTalent(StringUtil.removeCsvByte(buf));
		heroLevelUp.setStar(StringUtil.removeCsvByte(buf));
		heroLevelUp.setLevel(StringUtil.removeCsvByte(buf));
		heroLevelUp.setNeedExp(StringUtil.removeCsvInt(buf));
		return heroLevelUp;
	}

}
