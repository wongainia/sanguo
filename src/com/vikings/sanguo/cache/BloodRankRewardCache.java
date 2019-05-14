package com.vikings.sanguo.cache;

import java.util.Map.Entry;
import java.util.Set;

import com.vikings.sanguo.model.BloodRankReward;

public class BloodRankRewardCache extends FileCache {
	public static String FILE_NAME = "blood_rank_reward.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((BloodRankReward) obj).getRankLimit();
	}

	@Override
	public Object fromString(String line) {
		return BloodRankReward.fromString(line);
	}

	public BloodRankReward getBloodRankReward(int rank) {
		Set<Entry<Integer, BloodRankReward>> set = content.entrySet();
		for (Entry<Integer, BloodRankReward> entry : set) {
			BloodRankReward brr = entry.getValue();
			if (brr.getRankLimit() <= rank && brr.getRankCaps() >= rank)
				return brr;
		}
		return null;
	}

}
