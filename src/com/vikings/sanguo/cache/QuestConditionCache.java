package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.QuestCondition;

public class QuestConditionCache extends ArrayFileCache {

	private static final String FILE_NAME = "quest_condition.csv";

	@Override
	public long getSearchKey1(Object obj) {
		return ((QuestCondition) obj).getQuestId();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return ((QuestCondition) obj).getSequence();
	}

	@Override
	public Object fromString(String line) {
		return QuestCondition.fromString(line);
	}

	@Override
	public String getName() {
		return FILE_NAME;
	}

}
