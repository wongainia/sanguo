package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.Quest;

public class QuestCache extends FileCache {

	private static final String FILE_NAME = "prop_quest.csv";

	@Override
	public Object fromString(String line) {
		return Quest.fromString(line);
	}

	@Override
	public Object getKey(Object obj) {
		return ((Quest) obj).getId();
	}

	@Override
	public String getName() {
		return FILE_NAME;
	}

}
