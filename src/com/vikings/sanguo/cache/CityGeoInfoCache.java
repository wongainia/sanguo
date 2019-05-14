/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-1-9 下午5:33:15
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.CityGeoInfo;

public class CityGeoInfoCache extends LazyLoadArrayCache{
	private static final String FILE_NAME = "prop_city.csv";
	
	@Override
	public Object fromString(String line) {
		return CityGeoInfo.fromString(line);
	}

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public long getSearchKey1(Object obj) {
		return ((CityGeoInfo) obj).getProvince();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return ((CityGeoInfo) obj).getCity();
	}
}
