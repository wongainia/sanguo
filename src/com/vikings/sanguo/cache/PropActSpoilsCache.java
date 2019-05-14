/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-4-23 下午3:06:29
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.PropActSpoils;
import com.vikings.sanguo.utils.ListUtil;

public class PropActSpoilsCache extends ArrayFileCache{
	public static String FILE_NAME = "prop_act_spoils.csv";
	
	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object fromString(String line) {
		return PropActSpoils.fromString(line);
	}

	@Override
	public long getSearchKey1(Object obj) {
		return ((PropActSpoils)obj).getActId();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return ((PropActSpoils)obj).getType();
	}

	public boolean hasActSpoil(int actId) {
		return !ListUtil.isNull(search(actId));
	}
}
