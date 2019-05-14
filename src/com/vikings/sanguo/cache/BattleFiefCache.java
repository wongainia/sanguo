package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.BattleFief;

public class BattleFiefCache extends FileCache {

	private static final String FILE_NAME = "battle_fief.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((BattleFief) obj).getPropId();
	}

	@Override
	public Object fromString(String line) {
		return BattleFief.fromString(line);
	}

}
