/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-1-24 下午1:51:12
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.HolyPropCfg;

public class HolyPropCfgCache extends LazyLoadCache{

	@Override
	public String getName() {
		return "holy_prop.csv";
	}

	@Override
	public Object getKey(Object obj) {
		return ((HolyPropCfg)obj).getPropId();
	}

	@Override
	public Object fromString(String line) {
		return HolyPropCfg.fromString(line);
	}

}
