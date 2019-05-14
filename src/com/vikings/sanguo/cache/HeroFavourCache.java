package com.vikings.sanguo.cache;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.HeroFavour;

public class HeroFavourCache extends FileCache {
	private static final String FILE_NAME = "hero_favour.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((HeroFavour) obj).getLevel();
	}

	@Override
	public Object fromString(String line) {
		return HeroFavour.fromString(line);
	}	
	
	public int getFavourCost(int level) {
		int cost = 0;
		try 
		{
			HeroFavour hf = (HeroFavour) CacheMgr.heroFavourCache.get(level);
			cost = hf.getFavourCost();
		} 
		catch (GameException e) 
		{
			e.printStackTrace();
		}
		return cost;
	}
	
	public HeroFavour getHeroFavour(int level) 
	{
		HeroFavour hf = null;
		try 
		{
			hf = (HeroFavour) CacheMgr.heroFavourCache.get(level);
		} 
		catch (GameException e) 
		{			
			e.printStackTrace();
			return null;
		}
		return hf;
	}
}
