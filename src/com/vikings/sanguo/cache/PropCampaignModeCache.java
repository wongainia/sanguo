package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.PropCampaignMode;

public class PropCampaignModeCache extends FileCache {
	static public String FILE_NAME = "prop_campaign_mode.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object fromString(String line) {
		return PropCampaignMode.fromString(line);
	}

	@Override
	public Object getKey(Object obj) {
		return ((PropCampaignMode) obj).getId();
	}
}
