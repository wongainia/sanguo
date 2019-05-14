/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-5-24 下午4:18:14
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.BuildingStore;

public class BuildingStoreCache extends LazyLoadCache{

	@Override
	public String getName() {
		return "building_store.csv";
	}

	@Override
	public Object getKey(Object obj) {
		return ((BuildingStore)obj).getBuildingId();
	}

	@Override
	public Object fromString(String line) {
		return BuildingStore.fromString(line);
	}

}
