package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.HeroDevourExp;

public class HeroDevourExpCache extends FileCache {
	private static final String FILE_NAME = "hero_devour_exp.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((HeroDevourExp) obj).getHeroId();
	}

	@Override
	public Object fromString(String line) {
		return HeroDevourExp.fromString(line);
	}

}
