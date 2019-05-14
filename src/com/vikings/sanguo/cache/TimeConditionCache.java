package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.TimeCondition;

public class TimeConditionCache extends ArrayFileCache {
	public static String FILE_NAME = "prop_time_condition.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object fromString(String line) {
		return TimeCondition.fromString(line);
	}

	@Override
	public long getSearchKey1(Object obj) {
		return ((TimeCondition) obj).getId();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return buildKey(((TimeCondition) obj).getOpenTime(),
				((TimeCondition) obj).getCloseTime());
	}

}
