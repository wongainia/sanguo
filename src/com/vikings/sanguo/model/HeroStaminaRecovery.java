package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

//体力恢复配置
public class HeroStaminaRecovery {
	private int type; // 恢复体力方案
	private int cost; // 消耗元宝数值
	private int recovery; // 最大恢复体力数值
	private String desc; // 方案描述(客户端使用)

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getRecovery() {
		return recovery;
	}

	public void setRecovery(int recovery) {
		this.recovery = recovery;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public static HeroStaminaRecovery fromString(String csv) {
		HeroStaminaRecovery heroStaminaRecovery = new HeroStaminaRecovery();
		StringBuilder buf = new StringBuilder(csv);
		heroStaminaRecovery.setType(StringUtil.removeCsvInt(buf));
		heroStaminaRecovery.setCost(StringUtil.removeCsvInt(buf));
		heroStaminaRecovery.setRecovery(StringUtil.removeCsvInt(buf));
		heroStaminaRecovery.setDesc(StringUtil.removeCsv(buf));
		return heroStaminaRecovery;
	}
}
