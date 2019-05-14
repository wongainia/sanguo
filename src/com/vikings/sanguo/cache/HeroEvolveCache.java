package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.HeroEvolve;

public class HeroEvolveCache extends LazyLoadCache {
	private static final String FILE_NAME = "hero_evolve.csv";

	@Override
	public Object getKey(Object obj) {
		return ((HeroEvolve) obj).getHeroId();
	}

	@Override
	public Object fromString(String line) {
		return HeroEvolve.fromString(line);
	}

	@Override
	public String getName() {
		return FILE_NAME;
	}
}
