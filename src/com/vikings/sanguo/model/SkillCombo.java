package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class SkillCombo {
	private int id; // 组合技能ID
	private int hero1Id; // 第一个将领ID
	private int hero2Id; // 第二个将领ID
	private int hero3Id; // 第三个将领ID
	private int key;// 在高清图片中的组合技 有相同的组合技ID取 key值最小的

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getHero1Id() {
		return hero1Id;
	}

	public void setHero1Id(int hero1Id) {
		this.hero1Id = hero1Id;
	}

	public int getHero2Id() {
		return hero2Id;
	}

	public void setHero2Id(int hero2Id) {
		this.hero2Id = hero2Id;
	}

	public int getHero3Id() {
		return hero3Id;
	}

	public void setHero3Id(int hero3Id) {
		this.hero3Id = hero3Id;
	}

	public static SkillCombo fromString(String csv) {
		SkillCombo skillCombo = new SkillCombo();
		StringBuilder buf = new StringBuilder(csv);
		skillCombo.setId(StringUtil.removeCsvInt(buf));
		skillCombo.setHero1Id(StringUtil.removeCsvInt(buf));
		skillCombo.setHero2Id(StringUtil.removeCsvInt(buf));
		skillCombo.setHero3Id(StringUtil.removeCsvInt(buf));
		skillCombo.setKey(StringUtil.removeCsvInt(buf));
		return skillCombo;
	}
}
