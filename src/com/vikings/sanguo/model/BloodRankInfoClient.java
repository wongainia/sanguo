package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.BloodRankInfo;

public class BloodRankInfoClient {
	private int userId;
	private String nick;
	private int image;
	private int country;
	private int rank;
	private int guildId;
	private int bestRecord;// 最高关卡
	private int reward; // 是否领取过奖励, 1：领取过， 0:未领过

	private BriefGuildInfoClient bgic; // 家族信息(无家族为null)
	private Item rewardItem; // 奖励物品（无奖励为null）

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}

	public int getCountry() {
		return country;
	}

	public void setCountry(int country) {
		this.country = country;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public boolean hasRank() {
		return rank > 0;
	}

	public int getGuildId() {
		return guildId;
	}

	public void setGuildId(int guildId) {
		this.guildId = guildId;
	}

	public boolean hasGuild() {
		return guildId > 0 && null != bgic;
	}

	public int getBestRecord() {
		return bestRecord;
	}

	public void setBestRecord(int bestRecord) {
		this.bestRecord = bestRecord;
	}

	public int getReward() {
		return reward;
	}

	public void setReward(int reward) {
		this.reward = reward;
	}

	public boolean hasReward() { // 是否上榜有奖励
		return null != rewardItem;
	}

	public void award() { // 设置领取奖励
		reward = 1;
	}

	public boolean canReward() { // 是否有奖励且还未领取
		return hasReward() && reward == 0;
	}

	public BriefGuildInfoClient getBgic() {
		return bgic;
	}

	public void setBgic(BriefGuildInfoClient bgic) {
		this.bgic = bgic;
	}

	public Item getRewardItem() {
		return rewardItem;
	}

	public void setRewardItem(Item rewardItem) {
		this.rewardItem = rewardItem;
	}

	public static BloodRankInfoClient convert(BloodRankInfo info)
			throws GameException {
		if (null == info)
			return null;
		BloodRankReward brr = CacheMgr.bloodRankRewardCache
				.getBloodRankReward(info.getRank());
		return convert(info, brr);
	}

	private static BloodRankInfoClient convert(BloodRankInfo info,
			BloodRankReward brr) throws GameException {
		BloodRankInfoClient bric = new BloodRankInfoClient();
		bric.setUserId(info.getUserid());
		bric.setNick(info.getNick());
		bric.setImage(info.getImage());
		bric.setCountry(info.getCountry());
		bric.setRank(info.getRank());
		bric.setGuildId(info.getGuildid());
		bric.setBestRecord(info.getRankData());
		bric.setReward(info.getReward());
		if (null != brr)
			bric.setRewardItem(CacheMgr.getItemByID(brr.getRewardItemId()));
		return bric;
	}

	public static List<BloodRankInfoClient> convert2List(
			List<BloodRankInfo> infos) throws GameException {
		List<BloodRankInfoClient> brics = new ArrayList<BloodRankInfoClient>();
		if (null != infos) {
			BloodRankReward brr = null;
			for (BloodRankInfo info : infos) {
				if (null == brr || !brr.contains(info.getRank()))
					brr = CacheMgr.bloodRankRewardCache.getBloodRankReward(info
							.getRank());
				brics.add(convert(info, brr));
			}
		}
		return brics;
	}
}
