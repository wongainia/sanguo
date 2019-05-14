package com.vikings.sanguo.model;

import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;

public class WorldLevelInfoClient {
	// 当前世界等级
	public static int worldLevel;
	// 当前世界等级进度
	public static int worldLevelProcess;
	// 当前世界等级进度总量
	public static int worldLevelProcessTotal;

	private static WorldLevelProp prop;

	public static WorldLevelProp getWorldLevelProp() {
		if (null == prop || prop.getLevel() != worldLevel)
			try {
				prop = (WorldLevelProp) CacheMgr.worldLevelPropCache
						.get((byte) worldLevel);
			} catch (GameException e) {
				e.printStackTrace();
			}
		return prop;
	}

}
