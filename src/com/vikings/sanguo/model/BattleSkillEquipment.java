package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class BattleSkillEquipment {
	private int skillId; // 套装或者专属技能id
	private int heroId;
	private int equipId1;
	private int equipId2;
	private int equipId3;
	private int equipId4;
	private int key;

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public int getEquipId1() {
		return equipId1;
	}

	public void setEquipId1(int equipId1) {
		this.equipId1 = equipId1;
	}

	public int getEquipId2() {
		return equipId2;
	}

	public void setEquipId2(int equipId2) {
		this.equipId2 = equipId2;
	}

	public int getEquipId3() {
		return equipId3;
	}

	public void setEquipId3(int equipId3) {
		this.equipId3 = equipId3;
	}

	public int getEquipId4() {
		return equipId4;
	}

	public void setEquipId4(int equipId4) {
		this.equipId4 = equipId4;
	}

	public static BattleSkillEquipment fromString(String line) {
		BattleSkillEquipment bsq = new BattleSkillEquipment();
		StringBuilder buf = new StringBuilder(line);
		bsq.setSkillId(StringUtil.removeCsvInt(buf));
		bsq.setHeroId(StringUtil.removeCsvInt(buf));
		bsq.setEquipId1(StringUtil.removeCsvInt(buf));
		bsq.setEquipId2(StringUtil.removeCsvInt(buf));
		bsq.setEquipId3(StringUtil.removeCsvInt(buf));
		bsq.setEquipId4(StringUtil.removeCsvInt(buf));
		bsq.setKey(StringUtil.removeCsvInt(buf));
		return bsq;
	}

}
