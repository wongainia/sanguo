/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-4-2 上午10:19:36
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.PropTroopName;

public class PropTroopNameCache extends FileCache{
	final static String FILE_NAME = "prop_troop_name.csv";
	
	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((PropTroopName)obj).getType();
	}

	@Override
	public Object fromString(String line) {
		return PropTroopName.fromString(line);
	}

}
