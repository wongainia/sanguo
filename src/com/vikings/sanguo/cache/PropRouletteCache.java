package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.PropRoulette;

public class PropRouletteCache extends ArrayFileCache {
	private static final String FILE_NAME = "prop_roulette.csv";

	@Override
	public long getSearchKey1(Object obj) {
		return ((PropRoulette) obj).getLayer();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return ((PropRoulette) obj).getId();
	}

	@Override
	public Object fromString(String line) {
		return PropRoulette.fromString(line);
	}

	@Override
	public String getName() {
		return FILE_NAME;
	}
}
