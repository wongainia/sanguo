package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class BloodReward {
	private int scheme;// 将领方案
	private int recordLimit;// 关卡下限
	private int recordCaps;// 关卡上限
	private int rewardType;// 奖励类型
	private int rewardId;// 奖励id
	private int rewardCount;// 奖励数量
	private int addRate;// 奖励递增系数
	private String pokerImg;// 卡牌图标
	private String rewardName;// 奖品名称
	private String rewardImg;// 奖品图标

	public int getScheme() {
		return scheme;
	}

	public void setScheme(int scheme) {
		this.scheme = scheme;
	}

	public int getRecordLimit() {
		return recordLimit;
	}

	public void setRecordLimit(int recordLimit) {
		this.recordLimit = recordLimit;
	}

	public int getRecordCaps() {
		return recordCaps;
	}

	public void setRecordCaps(int recordCaps) {
		this.recordCaps = recordCaps;
	}

	public int getRewardType() {
		return rewardType;
	}

	public void setRewardType(int rewardType) {
		this.rewardType = rewardType;
	}

	public int getRewardId() {
		return rewardId;
	}

	public void setRewardId(int rewardId) {
		this.rewardId = rewardId;
	}

	public int getRewardCount() {
		return rewardCount;
	}

	public void setRewardCount(int rewardCount) {
		this.rewardCount = rewardCount;
	}

	public String getPokerImg() {
		return pokerImg;
	}

	public void setPokerImg(String pokerImg) {
		this.pokerImg = pokerImg;
	}

	public String getRewardName() {
		return rewardName;
	}

	public void setRewardName(String rewardName) {
		this.rewardName = rewardName;
	}

	public String getRewardImg() {
		return rewardImg;
	}

	public void setRewardImg(String rewardImg) {
		this.rewardImg = rewardImg;
	}

	public int getAddRate() {
		return addRate;
	}

	public void setAddRate(int addRate) {
		this.addRate = addRate;
	}

	public static BloodReward fromString(String line) {
		BloodReward br = new BloodReward();
		StringBuilder buf = new StringBuilder(line);
		br.setScheme(StringUtil.removeCsvInt(buf));
		br.setRecordLimit(StringUtil.removeCsvInt(buf));
		br.setRecordCaps(StringUtil.removeCsvInt(buf));
		br.setRewardType(StringUtil.removeCsvInt(buf));
		br.setRewardId(StringUtil.removeCsvInt(buf));
		br.setRewardCount(StringUtil.removeCsvInt(buf));
		br.setAddRate(StringUtil.removeCsvInt(buf));
		br.setPokerImg(StringUtil.removeCsv(buf));
		StringUtil.removeCsv(buf);
		StringUtil.removeCsv(buf);
		StringUtil.removeCsv(buf);
		br.setRewardName(StringUtil.removeCsv(buf));
		br.setRewardImg(StringUtil.removeCsv(buf));
		return br;
	}
}
