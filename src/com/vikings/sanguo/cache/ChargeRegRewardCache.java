/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-9-16 下午4:51:38
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.ChargeRegReward;

public class ChargeRegRewardCache extends ArrayFileCache {
	private final static String NAME = "charge_reg_reward.csv";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Object fromString(String line) {
		return ChargeRegReward.fromString(line);
	}

	public ChargeRegReward getChargeRegRewardCache(int time) {
		for (Object it : list) {
			ChargeRegReward crr = (ChargeRegReward)it;
			if (time >= Account.user.getRegTime() + crr.getStartSec()
					&& time <= Account.user.getRegTime() + crr.getEndSec())
				return crr;
		}
		
		return null;
	}

	@Override
	public long getSearchKey1(Object obj) {
		return ((ChargeRegReward) obj).getStartSec();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return ((ChargeRegReward) obj).getEndSec();
	}
}
