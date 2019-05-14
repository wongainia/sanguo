/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-6-7 下午12:13:48
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import com.vikings.sanguo.model.FirstRechargeReward;

public class FirstRechargeRewardCache extends FileCache{

	@Override
	public String getName() {
		return "first_recharge_reward.csv";
	}

	@Override
	public Object getKey(Object obj) {
		return ((FirstRechargeReward)obj).getId();
	}

	@Override
	public Object fromString(String line) {
		return FirstRechargeReward.fromString(line);
	}

	public ArrayList<FirstRechargeReward> getAll() {
		ArrayList<FirstRechargeReward> ls = new ArrayList<FirstRechargeReward>();
		Iterator<Entry<Integer, FirstRechargeReward>> it = content.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, FirstRechargeReward> entry = it.next();
			ls.add(entry.getValue());
		}
		return ls;
	}
}
