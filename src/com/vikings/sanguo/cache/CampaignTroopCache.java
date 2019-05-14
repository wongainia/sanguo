package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.CampaignTroop;

public class CampaignTroopCache extends LazyLoadArrayCache {
	public static String FILE_NAME = "prop_campaign_troop.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object fromString(String line) {
		return CampaignTroop.fromString(line);
	}

	@Override
	public long getSearchKey1(Object obj) {
		return ((CampaignTroop) obj).getId();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return ((CampaignTroop) obj).getTroopId();
	}

}
