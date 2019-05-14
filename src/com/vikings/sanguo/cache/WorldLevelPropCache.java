package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.WorldLevelProp;

public class WorldLevelPropCache extends FileCache {
	private static final String FILE_NAME = "worldlevel_prop.csv";

	public byte maxLevel;

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((WorldLevelProp) obj).getLevel();
	}

	@Override
	public Object fromString(String line) {
		WorldLevelProp prop = WorldLevelProp.fromString(line);
		if (prop.getLevel() > maxLevel)
			maxLevel = prop.getLevel();
		return prop;
	}

}
