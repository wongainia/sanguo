package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.HeroTroopName;

public class HeroTroopNameCache extends FileCache {

	private static final String FILE_NAME = "hero_troop_name.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((HeroTroopName) obj).getType();
	}

	@Override
	public Object fromString(String line) {
		return HeroTroopName.fromString(line);
	}

}
