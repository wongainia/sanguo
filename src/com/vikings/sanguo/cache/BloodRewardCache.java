package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.BloodReward;

public class BloodRewardCache extends FileCache {
	public static String FILE_NAME = "blood_reward.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((BloodReward) obj).getScheme();
	}

	@Override
	public Object fromString(String line) {
		return BloodReward.fromString(line);
	}

}
