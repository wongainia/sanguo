package com.vikings.sanguo.model;

import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.HeroShopItemInfo;

public class HeroShopItemInfoClient {
	private int id;
	private int boxId;
	private boolean bought;// 是否已经购买
	
	private boolean isRecruit;//已招募   用于在翻牌界面显示

	private HeroInit heroInit;
	private HeroProp heroProp;
	private HeroQuality heroQuality;

	private HeroShopItemInfoClient(int boxId) throws GameException {
		this.boxId = boxId;
		heroInit = (HeroInit) CacheMgr.heroInitCache.get(boxId);
		heroProp = (HeroProp) CacheMgr.heroPropCache.get(heroInit.getHeroId());
		heroQuality = (HeroQuality) CacheMgr.heroQualityCache.get(heroInit
				.getTalent());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBoxId() {
		return boxId;
	}

	public void setBoxId(int boxId) {
		this.boxId = boxId;
	}

	public boolean isBought() {
		return bought;
	}

	public void setBought(boolean bought) {
		this.bought = bought;
	}
	
	public boolean isRecruit() {
		return isRecruit;
	}

	public void setRecruit(boolean recruit) {
		this.isRecruit = recruit;
	}

	public HeroInit getHeroInit() {
		return heroInit;
	}

	public void setHeroInit(HeroInit heroInit) {
		this.heroInit = heroInit;
	}

	public HeroProp getHeroProp() {
		return heroProp;
	}

	public void setHeroProp(HeroProp heroProp) {
		this.heroProp = heroProp;
	}

	public HeroQuality getHeroQuality() {
		return heroQuality;
	}

	public void setHeroQuality(HeroQuality heroQuality) {
		this.heroQuality = heroQuality;
	}

	public static HeroShopItemInfoClient convert(HeroShopItemInfo info)
			throws GameException {
		if (null == info)
			return null;
		HeroShopItemInfoClient hsiic = new HeroShopItemInfoClient(
				info.getBoxid());
		hsiic.setId(info.getId());
		hsiic.setBought(info.getBought());
		return hsiic;
	}

}
