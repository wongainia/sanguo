/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-6-21 下午12:02:27
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.CampaignSpoilsAppend;

public class CampaignSpoilsAppendCache extends ArrayFileCache{
	
	@Override
	public String getName() {
		return "prop_campaign_spoils_append.csv";
	}

	@Override
	public long getSearchKey1(Object obj) {
		return ((CampaignSpoilsAppend)obj).getId();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return ((CampaignSpoilsAppend)obj).getType();
	}

	@Override
	public Object fromString(String line) {
		return CampaignSpoilsAppend.fromString(line);
	}

}
