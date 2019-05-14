package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.LevelUpDesc;

public class LevelupDescCache extends ArrayFileCache {

	private static final String FILE_NAME = "levelup_desc.csv";

	@Override
	public long getSearchKey1(Object obj) {
		return ((LevelUpDesc) obj).getLevel();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return ((LevelUpDesc) obj).getSeq();
	}

	@Override
	public Object fromString(String line) {
		return LevelUpDesc.fromString(line);
	}

	@Override
	public String getName() {
		return FILE_NAME;
	}
}
