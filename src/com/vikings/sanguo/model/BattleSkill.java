package com.vikings.sanguo.model;

import com.vikings.sanguo.R;
import com.vikings.sanguo.utils.StringUtil;

public class BattleSkill {
	public static final int MAINTYPE_ARM = 1;
	public static final int MAINTYPE_HERO = 2;
	public static final int MAINTYPE_GODDESS = 3;
	public static final int MAINTYPE_TERRAIN = 4;
	public static final int MAINTYPE_EQUIPMENT = 5;
	public static final int MAINTYPE_PATRONIZE = 6;
	private int id; // 技能ID
	private String name; // 技能名
	private byte mainType;// 1士兵技能 2英雄特技 3女神技能 4地形 5装备特技 6宠幸效果
	private byte canStudy;// 是否可学， 0不可学，1可学
	private String icon; // 图标
	private int type; // 类型
	private byte level; // 等级
	private String effectDesc;// 当前效果描述
	private int upgradeRate; // 升级成功率,后加百分比
	private byte skillAction;// 技能动作(0默认动作 1近战攻击 2远程射击 3远程法术)
	private int animType; // 技能动画类型

	private byte realMode;// 实现方式(1挂士兵 2挂将领 3挂兵种类型 4挂兵种ID)
	private byte addedValue;// 附加值
	private byte display;// 0内功，1招式
	private String effects; // 特效动画
	private byte rating;// 评价

	private int skillAt = 0; // 技能喊话

	private int gain = 0; // 增益/损益(0无意义 1增益 2损益——决定地图炮红蓝，地图炮方向由第一条效果目标决定

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte getMainType() {
		return mainType;
	}

	public void setMainType(byte mainType) {
		this.mainType = mainType;
	}

	public byte getCanStudy() {
		return canStudy;
	}

	public void setCanStudy(byte canStudy) {
		this.canStudy = canStudy;
	}

	public boolean canStudy() {
		return this.canStudy == 1;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public byte getLevel() {
		return level;
	}

	public void setLevel(byte level) {
		this.level = level;
	}

	public String getEffectDesc() {
		return effectDesc;
	}

	public void setEffectDesc(String effectDesc) {
		this.effectDesc = effectDesc;
	}

	public int getUpgradeRate() {
		return upgradeRate;
	}

	public void setUpgradeRate(int upgradeRate) {
		this.upgradeRate = upgradeRate;
	}

	public byte getSkillAction() {
		return skillAction;
	}

	public void setSkillAction(byte showSmallIconInBattle) {
		this.skillAction = showSmallIconInBattle;
	}

	// 得到评定的图片
	public int getRatingPic() {
		switch (getRating()) {
		case 4:
			return R.drawable.icon_jia;
		case 3:
			return R.drawable.icon_yi;
		case 2:
			return R.drawable.icon_bing;
		case 1:
			return R.drawable.icon_ding;

		default:
			return R.drawable.icon_ding;
		}
	}

	// 技能等级系数，用于计算将领战斗力
	public float getRatingAdd() {
		switch (getRating()) {
		case 1:
			return 1.2f;
		case 2:
			return 1.15f;
		case 3:
			return 1.10f;
		case 4:
			return 1.05f;
		default:
			return 1.05f;
		}
	}

	public int getAnimType() {
		return animType;
	}

	public void setAnimType(int animType) {
		this.animType = animType;
	}

	public static boolean isValidId(int skillId) {
		return skillId > 0;
	}

	public byte getRealMode() {
		return realMode;
	}

	public void setRealMode(byte realMode) {
		this.realMode = realMode;
	}

	public byte getAddedValue() {
		return addedValue;
	}

	public void setAddedValue(byte addedValue) {
		this.addedValue = addedValue;
	}

	public byte getDisplay() {
		return display;
	}

	public void setDsplay(byte display) {
		this.display = display;
	}

	public String getEffects() {
		return effects;
	}

	public void setEffects(String effects) {
		this.effects = effects;
	}

	public byte getRating() {
		return rating;
	}

	public void setRating(byte rating) {
		this.rating = rating;
	}

	public int getSkillAt() {
		return skillAt;
	}

	public void setSkillAt(int skillAt) {
		this.skillAt = skillAt;
	}

	public int getGain() {
		return gain;
	}

	public void setGain(int gain) {
		this.gain = gain;
	}

	public static BattleSkill fromString(String csv) {
		BattleSkill battleSkill = new BattleSkill();
		StringBuilder buf = new StringBuilder(csv);
		battleSkill.setId(StringUtil.removeCsvInt(buf));
		battleSkill.setName(StringUtil.removeCsv(buf));
		battleSkill.setMainType(StringUtil.removeCsvByte(buf));
		battleSkill.setCanStudy(StringUtil.removeCsvByte(buf));
		battleSkill.setIcon(StringUtil.removeCsv(buf));
		battleSkill.setType(StringUtil.removeCsvInt(buf));
		battleSkill.setLevel(StringUtil.removeCsvByte(buf));
		battleSkill.setEffectDesc(StringUtil.removeCsv(buf));
		battleSkill.setUpgradeRate(StringUtil.removeCsvInt(buf));
		battleSkill.setSkillAction(StringUtil.removeCsvByte(buf));
		battleSkill.setAnimType(StringUtil.removeCsvInt(buf));

		battleSkill.setRealMode(StringUtil.removeCsvByte(buf));
		battleSkill.setAddedValue(StringUtil.removeCsvByte(buf));
		battleSkill.setDsplay(StringUtil.removeCsvByte(buf));
		battleSkill.setEffects(StringUtil.removeCsv(buf));
		battleSkill.setRating(StringUtil.removeCsvByte(buf));
		battleSkill.setSkillAt(StringUtil.removeCsvInt(buf));

		battleSkill.setGain(StringUtil.removeCsvInt(buf));

		return battleSkill;
	}
}
