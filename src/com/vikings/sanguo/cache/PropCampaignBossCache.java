/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-4-23 下午3:12:35
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.PropCampaignBoss;

public class PropCampaignBossCache extends ArrayFileCache{
	public static String FILE_NAME = "prop_campaign_boss.csv";
	
	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object fromString(String line) {
		return PropCampaignBoss.fromString(line);
	}

	@Override
	public long getSearchKey1(Object obj) {
		return ((PropCampaignBoss)obj).getId();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return ((PropCampaignBoss)obj).getBossId();
	}

}
