package com.vikings.sanguo.cache;

import java.util.List;

import com.vikings.sanguo.model.PropCampaignSpoils;

public class PropCampaignSpoilsCache extends LazyLoadArrayCache {
	public static String FILE_NAME = "prop_campaign_spoils.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object fromString(String line) {
		return PropCampaignSpoils.fromString(line);
	}

	@Override
	public long getSearchKey1(Object obj) {
		return buildKey(((PropCampaignSpoils) obj).getId(),
				((PropCampaignSpoils) obj).getType());
	}

	@Override
	public long getSearchKey2(Object obj) {
		return ((PropCampaignSpoils) obj).getKey();
	}

	public List<PropCampaignSpoils> searchByIdAndType(int id, int type) {
		return search(buildKey(id, type));
	}

}
