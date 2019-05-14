package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class BloodRankReward {
	private int rankLimit;// 名次下限
	private int rankCaps; // 名次上限
	private int rewardItemId;// 奖品id（物品箱子）

	public int getRankLimit() {
		return rankLimit;
	}

	public void setRankLimit(int rankLimit) {
		this.rankLimit = rankLimit;
	}

	public int getRankCaps() {
		return rankCaps;
	}

	public void setRankCaps(int rankCaps) {
		this.rankCaps = rankCaps;
	}

	public int getRewardItemId() {
		return rewardItemId;
	}

	public void setRewardItemId(int rewardItemId) {
		this.rewardItemId = rewardItemId;
	}

	// 是否在区间内
	public boolean contains(int rank) {
		return rank >= rankLimit && rank <= rankCaps;
	}

	public static BloodRankReward fromString(String line) {
		BloodRankReward brr = new BloodRankReward();
		StringBuilder buf = new StringBuilder(line);
		brr.setRankLimit(StringUtil.removeCsvInt(buf));
		brr.setRankCaps(StringUtil.removeCsvInt(buf));
		brr.setRewardItemId(StringUtil.removeCsvInt(buf));
		return brr;
	}
}
