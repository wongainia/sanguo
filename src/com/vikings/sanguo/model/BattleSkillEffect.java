package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class BattleSkillEffect {

	private int id; // 技能 ID
	private int effectGroup;// 效果组ID
	private int groupProbability;// 组内概率
	private int target;// 目标
	private int targetGround;// 目标范围
	private int effectId;// 效果ID
	private int log;// 日志
	private int key;

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public int getLog() {
		return log;
	}

	public void setLog(int log) {
		this.log = log;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEffectGroup() {
		return effectGroup;
	}

	public void setEffectGroup(int effectGroup) {
		this.effectGroup = effectGroup;
	}

	public int getGroupProbability() {
		return groupProbability;
	}

	public void setGroupProbability(int groupProbability) {
		this.groupProbability = groupProbability;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public int getTargetGround() {
		return targetGround;
	}

	public void setTargetGround(int targetGround) {
		this.targetGround = targetGround;
	}

	public int getEffectId() {
		return effectId;
	}

	public void setEffectId(int effectId) {
		this.effectId = effectId;
	}

	public static BattleSkillEffect fromString(String csv) {
		BattleSkillEffect battleSkillEffect = new BattleSkillEffect();
		StringBuilder buf = new StringBuilder(csv);
		battleSkillEffect.setId(StringUtil.removeCsvInt(buf));
		battleSkillEffect.setEffectGroup(StringUtil.removeCsvInt(buf));
		battleSkillEffect.setGroupProbability(StringUtil.removeCsvInt(buf));
		battleSkillEffect.setTarget(StringUtil.removeCsvInt(buf));
		battleSkillEffect.setTargetGround(StringUtil.removeCsvInt(buf));
		battleSkillEffect.setEffectId(StringUtil.removeCsvInt(buf));
		battleSkillEffect.setLog(StringUtil.removeCsvInt(buf));
		battleSkillEffect.setKey(StringUtil.removeCsvInt(buf));
		return battleSkillEffect;
	}
}
