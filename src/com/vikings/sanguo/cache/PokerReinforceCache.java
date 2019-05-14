/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-11-6 下午3:00:31
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.PokerReinforce;

public class PokerReinforceCache extends LazyLoadCache{
	static final String NAME = "poker_reinforce.csv";
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((PokerReinforce)obj).getType();
	}

	@Override
	public Object fromString(String line) {
		return PokerReinforce.fromString(line);
	}


}
