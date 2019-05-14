package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

//战役中刷boss方案信息 
public class PropCampaignBoss {
	private int id; // 刷BOSS方案ID
	private int bossId; // bossId
	private int rate; // 出场概率（万分比）
	private int firtPassSpoilId; // BOSS对应的奖励方案ID（需通关才可获得）
	private int passSpoilId; // 后续通关的奖励方案ID

	public void setBossId(int bossId) {
		this.bossId = bossId;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public void setFirtPassSpoilId(int firtPassSpoilId) {
		this.firtPassSpoilId = firtPassSpoilId;
	}

	public void setPassSpoilId(int passSpoilId) {
		this.passSpoilId = passSpoilId;
	}

	public int getBossId() {
		return bossId;
	}

	public int getId() {
		return id;
	}

	public int getRate() {
		return rate;
	}

	public int getFirtPassSpoilId() {
		return firtPassSpoilId;
	}

	public int getPassSpoilId() {
		return passSpoilId;
	}

	public static PropCampaignBoss fromString(String line) {
		StringBuilder buf = new StringBuilder(line);
		PropCampaignBoss cb = new PropCampaignBoss();

		cb.setId(StringUtil.removeCsvInt(buf));
		cb.setBossId(StringUtil.removeCsvInt(buf));
		cb.setRate(StringUtil.removeCsvInt(buf));
		cb.setFirtPassSpoilId(StringUtil.removeCsvInt(buf));
		cb.setPassSpoilId(StringUtil.removeCsvInt(buf));

		return cb;
	}
}
