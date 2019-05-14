package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.UserNameRandom;

public class UserNameRandomCache extends ArrayFileCache {
	private static final String FILE_NAME = "username_random.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object fromString(String line) {
		return UserNameRandom.fromString(line);
	}

	@Override
	public long getSearchKey1(Object obj) {
		return ((UserNameRandom) obj).getSex();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return ((UserNameRandom) obj).getId();
	}

	public void clear() {
		list.clear();
		content.clear();
	}
}
