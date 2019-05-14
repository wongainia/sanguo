package com.vikings.sanguo.cache;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.HeroFavourConvert;

public class HeroFavourConvertCache extends FileCache {

	private static final String FILE_NAME = "hero_favour_convert.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((HeroFavourConvert) obj).getHeroId();
	}

	@Override
	public Object fromString(String line) {
		return HeroFavourConvert.fromString(line);
	}
	
	public int getConvertHeroId(int heroId) {
		int convertHeroId = 0;
		try 
		{
			HeroFavourConvert heroConvert = (HeroFavourConvert) CacheMgr.heroFavourConvertCache.get(heroId);
			convertHeroId = heroConvert.getNewHeroId();
		} 
		catch (GameException e) 
		{
			e.printStackTrace();
		}
		return convertHeroId;
	}
}
