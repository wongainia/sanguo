package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.PropFiefBlank;

public class PropFiefBlankCache extends FileCache {
	private static final String FILE_NAME = "prop_fief_blank.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((PropFiefBlank) obj).getId();
	}

	@Override
	public Object fromString(String line) {
		return PropFiefBlank.fromString(line);
	}

}
