package com.vikings.sanguo.model;

import java.util.List;

import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.HeroFavourInfo;

public class HeroFavourInfoClient {

	private int level;

	private int exp; // 进度
	private int expUpdateTime;// 进度更新时间
	private List<Integer> userSlots; // 已选择过的宠幸方式------->使用道具标记
	private List<Integer> userExps;// 已出现过的进度值------->加成值
	private HeroFavour heroFavour;// 宠幸相关

	public HeroFavourInfoClient(int level) throws GameException {
		this.level = level;
		heroFavour = CacheMgr.heroFavourCache.getHeroFavour(level);
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getExpUpdateTime() {
		return expUpdateTime;
	}

	public void setExpUpdateTime(int expUpdateTime) {
		this.expUpdateTime = expUpdateTime;
	}

	public List<Integer> getUserSlots() {
		return userSlots;
	}

	public void setUserSlots(List<Integer> userSlots) {
		this.userSlots = userSlots;
	}

	public boolean hasValue(int id) {
		return userSlots.contains(id);
	}

	public List<Integer> getUserExps() {
		return userExps;
	}

	public void setUserExps(List<Integer> userExps) {
		this.userExps = userExps;
	}

	public HeroFavour getHeroFavour() {
		return heroFavour;
	}

	public void setHeroFavour(HeroFavour heroFavour) {
		this.heroFavour = heroFavour;
	}

	public static HeroFavourInfoClient convert(HeroFavourInfo info)
			throws GameException {
		int level = 0;
		if (null != info)
			level = info.getLevel();
		HeroFavourInfoClient hfic = new HeroFavourInfoClient(level);
		hfic.setExp(info.getExp());
		hfic.setExpUpdateTime(info.getExpUpdateTime());
		hfic.setUserSlots(info.getUsedSlotsList());
		hfic.setUserExps(info.getUsedExpsList());
		return hfic;
	}
}
