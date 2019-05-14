package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.Plunder;

public class PlunderCache extends FileCache {
	private static final String FILE_NAME = "plunder.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((Plunder) obj).getType();
	}

	@Override
	public Object fromString(String line) {
		return Plunder.fromString(line);
	}

}
