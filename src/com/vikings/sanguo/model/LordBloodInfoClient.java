package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.LordBloodInfo;

public class LordBloodInfoClient {
	private List<ArmInfoClient> bloodTroopInfo;// 血战部队信息
	private List<HeroIdInfoClient> bloodHeroIdInfos;// 血战将领信息
	private int bloodCount; // 血战累计次数
	private int bloodRecord; // 血战关卡
	private int bloodRecordMax; // 今日血战最大关卡
	private int bloodRecordLast; // 血战上次关卡
	private int bloodReward; // 血战奖励标记 1 全量奖励 2 减半奖励
	private List<BloodPokerClient> bloodPokers; // 血战奖励
	private int bloodResetTime; // 血战相关统计数据重置时间

	public List<ArmInfoClient> getBloodTroopInfo() {
		return bloodTroopInfo == null ? new ArrayList<ArmInfoClient>()
				: bloodTroopInfo;
	}

	public List<ArmInfoClient> copyBloodTroopInfo() {
		List<ArmInfoClient> newlist = new ArrayList<ArmInfoClient>();
		if (bloodTroopInfo != null && !bloodTroopInfo.isEmpty()) {
			for (ArmInfoClient aic : bloodTroopInfo) {
				newlist.add(aic.copy());
			}
		}
		return newlist;
	}

	public void setBloodTroopInfo(List<ArmInfoClient> bloodTroopInfo) {
		this.bloodTroopInfo = bloodTroopInfo;
	}

	public List<HeroIdInfoClient> getBloodHeroIdInfos() {
		return bloodHeroIdInfos == null ? new ArrayList<HeroIdInfoClient>()
				: bloodHeroIdInfos;
	}

	public void setBloodHeroIdInfos(List<HeroIdInfoClient> bloodHeroIdInfos) {
		this.bloodHeroIdInfos = bloodHeroIdInfos;
	}

	public int getBloodCount() {
		return bloodCount;
	}

	public void setBloodCount(int bloodCount) {
		this.bloodCount = bloodCount;
	}

	public int getBloodRecord() {
		return bloodRecord;
	}

	public void setBloodRecord(int bloodRecord) {
		this.bloodRecord = bloodRecord;
	}

	public int getBloodRecordMax() {
		return bloodRecordMax;
	}

	public void setBloodRecordMax(int bloodRecordMax) {
		this.bloodRecordMax = bloodRecordMax;
	}

	public int getBloodRecordLast() {
		return bloodRecordLast;
	}

	public void setBloodRecordLast(int bloodRecordLast) {
		this.bloodRecordLast = bloodRecordLast;
	}

	public int getBloodReward() {
		return bloodReward;
	}

	public void setBloodReward(int bloodReward) {
		this.bloodReward = bloodReward;
	}

	public List<BloodPokerClient> getBloodPokers() {
		return bloodPokers == null ? new ArrayList<BloodPokerClient>()
				: bloodPokers;
	}

	public void setBloodPokers(List<BloodPokerClient> bloodPokers) {
		this.bloodPokers = bloodPokers;
	}

	public void updateBloodPoker(int index, BloodPokerClient bpc) {
		if (null == bpc || bloodPokers == null
				|| bloodPokers.size() < index + 1)
			return;
		BloodPokerClient client = bloodPokers.get(index);
		client.update(bpc);
	}

	// 取已经翻的牌总数
	public int getBloodPokerOpenCount() {
		int count = 0;
		for (BloodPokerClient bpc : getBloodPokers()) {
			if (bpc.isOpen())
				count++;
		}
		return count;
	}

	public int getBloodResetTime() {
		return bloodResetTime;
	}

	public void setBloodResetTime(int bloodResetTime) {
		this.bloodResetTime = bloodResetTime;
	}

	// 统计数据是否已经过了重置时间
	public boolean isValide() {
		return bloodResetTime > Config.serverTimeSS();
	}

	// 是否可以领奖
	public boolean hasReward() {
		return !getBloodPokers().isEmpty();
	}

	public boolean isLoss() {// 是否失败    失败才可以领取奖励   是全量
		return bloodReward == 1;
	}

	public static LordBloodInfoClient convert(LordBloodInfo info)
			throws GameException {
		if (null == info)
			return null;
		LordBloodInfoClient lbic = new LordBloodInfoClient();
		lbic.setBloodTroopInfo(ArmInfoClient.convertList(info
				.getBloodTroopInfo()));
		lbic.setBloodHeroIdInfos(HeroIdInfoClient.convert2List(info
				.getBloodHeroInfosList()));
		lbic.setBloodCount(info.getBloodCount());
		lbic.setBloodRecord(info.getBloodRecord());
		lbic.setBloodRecordMax(info.getBloodRecordMax());
		lbic.setBloodRecordLast(info.getBloodRecordLast());
		lbic.setBloodReward(info.getBloodReward());
		if (info.hasBloodPokers()) {
			lbic.setBloodPokers(BloodPokerClient.convert2List(info
					.getBloodPokersList()));
		}
		lbic.setBloodResetTime(info.getBloodResetTime());
		return lbic;
	}
}
