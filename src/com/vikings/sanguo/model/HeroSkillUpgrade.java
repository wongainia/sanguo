package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

//英雄技能升级配置
public class HeroSkillUpgrade {
	private int skillID;
	private int itemID;
	private int count;

	public int getSkillID() {
		return skillID;
	}

	public void setSkillID(int skillID) {
		this.skillID = skillID;
	}

	public int getItemID() {
		return itemID;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public static HeroSkillUpgrade fromString(String csv) {
		HeroSkillUpgrade heroSkillUpgrade = new HeroSkillUpgrade();
		StringBuilder buf = new StringBuilder(csv);
		heroSkillUpgrade.setSkillID(StringUtil.removeCsvInt(buf));
		heroSkillUpgrade.setItemID(StringUtil.removeCsvInt(buf));
		heroSkillUpgrade.setCount(StringUtil.removeCsvInt(buf));
		return heroSkillUpgrade;
	}
}
