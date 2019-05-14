package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class HeroFavour {
	private int level;// 兴奋值等级
	private int attackEnhan;// 增加的武力值
	private int defendEnhan; // 增加的防护值
	private int favourCost;// 使用道具消耗元宝

	public static HeroFavour fromString(String csv) {
		HeroFavour heroFavour = new HeroFavour();
		StringBuilder buf = new StringBuilder(csv);
		heroFavour.setLevel(StringUtil.removeCsvInt(buf));
		heroFavour.setAttackEnhan(StringUtil.removeCsvInt(buf));
		heroFavour.setDefendEnhan(StringUtil.removeCsvInt(buf));
		heroFavour.setFavourCost(StringUtil.removeCsvInt(buf));
		return heroFavour;
	}

	public int getLevel() {
		return level;
	}

	public int getAttackEnhan() {
		return attackEnhan;
	}

	public int getDefendEnhan() {
		return defendEnhan;
	}

	public int getFavourCost() {
		return favourCost;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setAttackEnhan(int attackEnhan) {
		this.attackEnhan = attackEnhan;
	}

	public void setDefendEnhan(int defendEnhan) {
		this.defendEnhan = defendEnhan;
	}

	public void setFavourCost(int favourCost) {
		this.favourCost = favourCost;
	}
}
