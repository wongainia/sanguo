package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.LordArenaInfo;
import com.vikings.sanguo.utils.ListUtil;

public class LordArenaInfoClient {
	private List<ArmInfoClient> arenaTroopInfo; // 巅峰战场派兵信息
	private List<HeroIdInfoClient> arenaHeroIdInfos;// 巅峰战场将领信息
	private int arenaRank; // 巅峰战场名次
	private int arenaRewardStart; // 巅峰战场奖励计算开始时间
	private int arenaRewardStartValue; // 巅峰战场奖励计算积累值
	private int arenaCount; // 巅峰战场累计次数
	private int arenaCountResetTime;// 巅峰战场累计次数重置时间

	public List<ArmInfoClient> getArenaTroopInfo() {
		return arenaTroopInfo == null ? new ArrayList<ArmInfoClient>()
				: arenaTroopInfo;
	}

	public void setArenaTroopInfo(List<ArmInfoClient> arenaTroopInfo) {
		this.arenaTroopInfo = arenaTroopInfo;
	}

	public List<HeroIdInfoClient> getArenaHeroIdInfos() {
		return arenaHeroIdInfos == null ? new ArrayList<HeroIdInfoClient>()
				: arenaHeroIdInfos;
	}

	public void setArenaHeroIdInfos(List<HeroIdInfoClient> arenaHeroIdInfos) {
		this.arenaHeroIdInfos = arenaHeroIdInfos;
	}

	public int getArenaRank() {
		return arenaRank;
	}

	public void setArenaRank(int arenaRank) {
		this.arenaRank = arenaRank;
	}

	public int getArenaRewardStart() {
		return arenaRewardStart;
	}

	public void setArenaRewardStart(int arenaRewardStart) {
		this.arenaRewardStart = arenaRewardStart;
	}

	public int getArenaRewardStartValue() {
		return arenaRewardStartValue;
	}

	public void setArenaRewardStartValue(int arenaRewardStartValue) {
		this.arenaRewardStartValue = arenaRewardStartValue;
	}

	public void setArenaCount(int arenaCount) {
		this.arenaCount = arenaCount;
	}

	public int getArenaCountResetTime() {
		return arenaCountResetTime;
	}

	public void setArenaCountResetTime(int arenaCountResetTime) {
		this.arenaCountResetTime = arenaCountResetTime;
	}

	public int getArenaCount() {
		if (arenaCountResetTime != 0
				&& Config.serverTimeSS() >= arenaCountResetTime) {
			return 0;
		}
		return arenaCount;
	}

	public int[] getCurArenaExploit() {
		ArenaReward ar = CacheMgr.arenaRewardCache.getArenaReward(arenaRank);
		int[] exploit = new int[2];
		if (null != ar) {
			float temp = (int) (arenaRewardStartValue + (Config.serverTimeSS() - arenaRewardStart)
					/ 3600f * ar.getReward());
			exploit[1] = ar.getLimit();

			if (temp < 0)
				temp = 0;

			if (temp > exploit[1])
				temp = exploit[1];

			if (CacheMgr.arenaResetTimeCache.canGetAward())
				temp = exploit[1];

			exploit[0] = (int) temp;
		} else {
			exploit[0] = 0;
			exploit[1] = 0;
		}

		return exploit;
	}

	public boolean isHeroInArena(long hero) {
		if (ListUtil.isNull(arenaHeroIdInfos))
			return false;

		for (HeroIdInfoClient it : arenaHeroIdInfos) {
			if (hero == it.getId())
				return true;
		}

		return false;
	}

	public boolean isArenaConfig() {
		return !ListUtil.isNull(arenaHeroIdInfos);
	}

	public static LordArenaInfoClient convert(LordArenaInfo info)
			throws GameException {
		if (null == info)
			return null;
		LordArenaInfoClient laic = new LordArenaInfoClient();
		laic.setArenaTroopInfo(ArmInfoClient.convertList(info
				.getArenaTroopInfo()));
		laic.setArenaHeroIdInfos(HeroIdInfoClient.convert2List(info
				.getArenaHeroInfosList()));
		laic.setArenaRank(info.getArenaRank());
		laic.setArenaRewardStart(info.getArenaRewardStart());
		laic.setArenaRewardStartValue(info.getArenaRewardStartValue());
		laic.setArenaCount(info.getArenaCount());
		laic.setArenaCountResetTime(info.getArenaCountResetTime());
		return laic;
	}

}
