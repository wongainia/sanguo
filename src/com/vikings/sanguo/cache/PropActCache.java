/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-4-23 下午2:29:48
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.PropAct;

public class PropActCache extends FileCache{
	public static String FILE_NAME = "prop_act.csv";
	
	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((PropAct)obj).getId();
	}

	@Override
	public Object fromString(String line) {
		return PropAct.fromString(line);
	}

}
