package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class HeroFavourConvert
{
	private int heroId;// 将领id
	private int newHeroId;// 新将领id
	
	public static HeroFavourConvert fromString(String csv) {
		HeroFavourConvert heroFavourConvert = new HeroFavourConvert();
		StringBuilder buf = new StringBuilder(csv);
		heroFavourConvert.setHeroId(StringUtil.removeCsvInt(buf));
		heroFavourConvert.setNewHeroId(StringUtil.removeCsvInt(buf));
		return heroFavourConvert;
	}
	
	public int getHeroId() {
		return heroId;
	}

	public int getNewHeroId() {
		return newHeroId;
	}
	
	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public void setNewHeroId(int newHeroId) {
		 this.newHeroId = newHeroId;
	}
}
