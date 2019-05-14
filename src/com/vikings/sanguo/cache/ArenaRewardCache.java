package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.ArenaReward;
import com.vikings.sanguo.model.Dict;

public class ArenaRewardCache extends ArrayFileCache {
	public static String FILE_NAME = "arena_reward.csv";

	@Override
	public long getSearchKey1(Object obj) {
		return ((ArenaReward) obj).getLow();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return ((ArenaReward) obj).getUp();
	}

	@Override
	public Object fromString(String line) {
		return ArenaReward.fromString(line);
	}

	@Override
	public String getName() {
		return FILE_NAME;
	}

	/**
	 * 取排行奖励
	 * 
	 * @param position
	 *            自己的排行
	 * @return
	 */
	public ArenaReward getArenaReward(int position) {
		for (Object obj : list) {
			ArenaReward arenaReward = (ArenaReward) obj;
			if (arenaReward.getLow() <= position
					&& arenaReward.getUp() >= position) {
				return arenaReward;
			}
		}
		return null;
	}

	public int getSpecificExploit(int rank) {
		int inteval = CacheMgr.dictCache.getDictInt(Dict.TYPE_ARENA, 4);
		ArenaReward ar = getArenaReward(rank);
		if (null != ar)
			return (int) (ar.getReward() / 3600f * inteval);
		return 0;
	}
}
