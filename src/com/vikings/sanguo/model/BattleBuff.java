/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-6-21 下午8:04:04
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class BattleBuff {
	private int id; // buffID
	private String name; // buff名称
	private String icon; // 技能图标
	private String effect; // 效果文字（战斗日志内显示）
	private int lastTimes; // 叠加次数
	private int extra; // 叠加值（效果基础值的百分比）

	private int clearance;// 清除类型(0大回合结束后消失 1攻击后消失 2受击后消失 3小回合结束后消失 4战斗结束后消失)
	private int triggerPhase;// 触发阶段
	private int triggerconditions1; // 触发条件1
	private int triggerconditions2; // 触发条件2
	private int triggerconditions3; // 触发条件3
	private int iconDisplay; // 图标是否显示(0不显示 1显示)
	private int isGain;// 增益/减益(0减益 1增益）

	public String getGainTxt() {
		if (isGain()) {
			return "增益";
		}
		return "减益";
	}

	public boolean isShowIcon() {
		return iconDisplay == 1;
	}

	public int getIconDisplay() {
		return iconDisplay;
	}

	public int getIsGain() {
		return isGain;
	}

	public boolean isGain() {
		return isGain == 1;
	}

	public void setIsGain(int isGain) {
		this.isGain = isGain;
	}

	public int getClearance() {
		return clearance;
	}

	public int getTriggerPhase() {
		return triggerPhase;
	}

	public int getTriggerconditions1() {
		return triggerconditions1;
	}

	public int getTriggerconditions2() {
		return triggerconditions2;
	}

	public int getTriggerconditions3() {
		return triggerconditions3;
	}

	public void setIconDisplay(int iconDisplay) {
		this.iconDisplay = iconDisplay;
	}

	public void setEffect(String effect) {
		this.effect = effect;
	}

	public void setExtra(int extra) {
		this.extra = extra;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setLastTimes(int lastTimes) {
		this.lastTimes = lastTimes;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEffect() {
		return effect;
	}

	public int getExtra() {
		return extra;
	}

	public String getIcon() {
		return icon;
	}

	public int getId() {
		return id;
	}

	public int getLastTimes() {
		return lastTimes;
	}

	public String getName() {
		return name;
	}

	public void setIconDisplay(byte iconDisplay) {
		this.iconDisplay = iconDisplay;
	}

	public void setTriggerPhase(int triggerPhase) {
		this.triggerPhase = triggerPhase;
	}

	public void setTriggerconditions1(int triggerconditions1) {
		this.triggerconditions1 = triggerconditions1;
	}

	public void setTriggerconditions2(int triggerconditions2) {
		this.triggerconditions2 = triggerconditions2;
	}

	public void setTriggerconditions3(int triggerconditions3) {
		this.triggerconditions3 = triggerconditions3;
	}

	public void setClearance(int clearance) {
		this.clearance = clearance;
	}

	public static BattleBuff fromString(String line) {
		StringBuilder buf = new StringBuilder(line);
		BattleBuff bb = new BattleBuff();

		bb.id = StringUtil.removeCsvInt(buf);
		bb.name = StringUtil.removeCsv(buf);
		bb.icon = StringUtil.removeCsv(buf);
		bb.effect = StringUtil.removeCsv(buf);

		bb.lastTimes = StringUtil.removeCsvInt(buf);
		bb.extra = StringUtil.removeCsvInt(buf);

		bb.clearance = StringUtil.removeCsvInt(buf);
		bb.triggerPhase = StringUtil.removeCsvInt(buf);
		bb.triggerconditions1 = StringUtil.removeCsvInt(buf);
		bb.triggerconditions2 = StringUtil.removeCsvInt(buf);
		bb.triggerconditions3 = StringUtil.removeCsvInt(buf);
		bb.iconDisplay = StringUtil.removeCsvInt(buf);
		bb.isGain = StringUtil.removeCsvInt(buf);

		return bb;
	}
}
