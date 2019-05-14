package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class PropUser {

	private int level; // 玩家等级

	private int expTotal; // 升级经验差

	private int heroLimit; // 将领数量上限

	private int rewardsItemId; // 升级奖励物品id

	private int resourceLimit;// 资源上限(每种)

	private int fiefLimit;// 领地上限(主城+资源点+据点+圣都……)

	public int getExpTotal() {
		return expTotal;
	}

	public void setExpTotal(int expTotal) {
		this.expTotal = expTotal;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getHeroLimit() {
		return heroLimit;
	}

	public void setHeroLimit(int heroLimit) {
		this.heroLimit = heroLimit;
	}

	public int getRewardsItemId() {
		return rewardsItemId;
	}

	public void setRewardsItemId(int rewardsItemId) {
		this.rewardsItemId = rewardsItemId;
	}

	public int getResourceLimit() {
		return resourceLimit;
	}

	public void setResourceLimit(int resourceLimit) {
		this.resourceLimit = resourceLimit;
	}

	public int getFiefLimit() {
		return fiefLimit;
	}

	public void setFiefLimit(int fiefLimit) {
		this.fiefLimit = fiefLimit;
	}

	public static PropUser fromString(String csv) {
		PropUser prop = new PropUser();
		StringBuilder buf = new StringBuilder(csv);
		prop.setLevel(StringUtil.removeCsvInt(buf));
		prop.setExpTotal(StringUtil.removeCsvInt(buf));
		prop.setHeroLimit(StringUtil.removeCsvInt(buf));
		prop.setRewardsItemId(StringUtil.removeCsvInt(buf));
		prop.setResourceLimit(StringUtil.removeCsvInt(buf));
		prop.setFiefLimit(StringUtil.removeCsvInt(buf));
		return prop;
	}
}
