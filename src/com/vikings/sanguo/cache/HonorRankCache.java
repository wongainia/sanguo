package com.vikings.sanguo.cache;

import java.util.List;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.HonorRank;
import com.vikings.sanguo.model.Item;

public class HonorRankCache extends ArrayFileCache {
	public static final String NAME = "honor_rank.csv";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Object fromString(String line) {
		return HonorRank.fromString(line);
	}

	@Override
	public long getSearchKey1(Object obj) {
		return ((HonorRank) obj).getTypeId();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return 0;
	}

	public Item getItem(int type, int pos) {
		List<HonorRank> hrs = search(type);
		for (HonorRank it : hrs) {
			if (pos >= it.getFromPlace() && pos <= it.getToPlace()) {
				try {
					return (Item) CacheMgr.itemCache.get(it.getItemId());
				} catch (GameException e) {
					return null;
				}
			}
		}
		return null;
	}

	public boolean isGtMaxRewardPos(int type, int pos) {
		List<HonorRank> hrs = search(type);
		int place = 0;
		for (HonorRank it : hrs) {
			if (it.getToPlace() > place) {
				place = it.getToPlace();
			}
		}

		return pos > place;
	}
}
