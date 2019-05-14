package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class BattleBuffEffect {

	private int id; // buff ID
	private int effectGroup;// 效果组
	private int groupProbability;// 组内概率
	private int target;// 目标
	private int count;// 数量
	private int effectId;// 效果ID
	private int log;// 日志
	private int key;

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
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

	public int getEffectId() {
		return effectId;
	}

	public void setEffectId(int effectId) {
		this.effectId = effectId;
	}

	public static BattleBuffEffect fromString(String csv) {
		BattleBuffEffect battleBuffEffect = new BattleBuffEffect();
		StringBuilder buf = new StringBuilder(csv);
		battleBuffEffect.setId(StringUtil.removeCsvInt(buf));
		battleBuffEffect.setEffectGroup(StringUtil.removeCsvInt(buf));
		battleBuffEffect.setGroupProbability(StringUtil.removeCsvInt(buf));
		battleBuffEffect.setTarget(StringUtil.removeCsvInt(buf));
		battleBuffEffect.setCount(StringUtil.removeCsvInt(buf));
		battleBuffEffect.setEffectId(StringUtil.removeCsvInt(buf));
		battleBuffEffect.setLog(StringUtil.removeCsvInt(buf));
		battleBuffEffect.setKey(StringUtil.removeCsvInt(buf));
		return battleBuffEffect;
	}
}
