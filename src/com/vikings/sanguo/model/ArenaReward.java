package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class ArenaReward {
	private int low;// 最低排名
	private int up;// 最高排名
	private int reward;// 每3600秒增加功勋值
	private int limit;// 储量上限

	public int getLow() {
		return low;
	}

	public void setLow(int low) {
		this.low = low;
	}

	public int getUp() {
		return up;
	}

	public void setUp(int up) {
		this.up = up;
	}

	public int getReward() {
		return reward;
	}

	public void setReward(int reward) {
		this.reward = reward;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public static ArenaReward fromString(String line) {
		ArenaReward arenaReward = new ArenaReward();
		StringBuilder buf = new StringBuilder(line);
		arenaReward.setLow(StringUtil.removeCsvInt(buf));
		arenaReward.setUp(StringUtil.removeCsvInt(buf));
		arenaReward.setReward(StringUtil.removeCsvInt(buf));
		arenaReward.setLimit(StringUtil.removeCsvInt(buf));
		return arenaReward;
	}
}
