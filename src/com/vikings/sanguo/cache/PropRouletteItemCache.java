package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.PropRouletteItem;

public class PropRouletteItemCache extends FileCache {
	private static final String FILE_NAME = "prop_roulette_item.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((PropRouletteItem) obj).getItemId();
	}

	@Override
	public Object fromString(String line) {
		return PropRouletteItem.fromString(line);
	}
}
