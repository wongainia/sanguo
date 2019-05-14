/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-4-23 下午2:55:53
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.PropCampaign;

public class PropCampaignCache extends FileCache{
	static public String FILE_NAME = "prop_campaign.csv"; 
	
	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((PropCampaign)obj).getId();
	}

	@Override
	public Object fromString(String line) {
		return PropCampaign.fromString(line);
	}
	
}
