package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.model.PokerConfig;

public class PokerConfigCache extends LazyLoadCache {
	private static final String FILE_NAME = "poker_config.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((PokerConfig) obj).getId();
	}

	@Override
	public Object fromString(String line) {
		return PokerConfig.fromString(line);
	}

	public List<PokerConfig> getAllPokerConfig() {
		checkLoad();
		return new ArrayList<PokerConfig>(getContent().values());
	}

}
