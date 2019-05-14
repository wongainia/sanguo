package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.VipRewards;

public class VipRewardsCache extends ArrayFileCache {

	private static final String FILE_NAME = "vip_rewards.csv";

	@Override
	public long getSearchKey1(Object obj) {
		return ((VipRewards) obj).getVipLevel();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return buildKey(((VipRewards) obj).getType(),
				((VipRewards) obj).getId());
	}

	@Override
	public Object fromString(String line) {
		return VipRewards.fromString(line);
	}

	@Override
	public String getName() {
		return FILE_NAME;
	}

}
