/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-9-17 下午7:47:06
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.cache;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.ShopDailyDiscount;

public class ShopDailyDiscountCache extends ArrayFileCache {
	private final static String NAME = "shop_daily_discount.csv";
	private int maxGroupId;

	@Override
	public synchronized void init() throws GameException {
		super.init();

		for (Object it : list) {
			ShopDailyDiscount sdd = (ShopDailyDiscount) it;
			if (sdd.getGroupId() > maxGroupId)
				maxGroupId = sdd.getGroupId();
		}
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Object fromString(String line) {
		return ShopDailyDiscount.fromString(line);
	}

	//TODO 每日折扣
	public ShopDailyDiscount getDailyDiscount(byte type,int itemId) {
		if (0 == maxGroupId)
			return null;

		// (（总时间+8小时）/天)%总组数 + 1
		int groupId = (int) (((Config.serverTimeSS() + 8 * Constants.HOUR) / (Constants.DAY))
				% maxGroupId + 1);

		ShopDailyDiscount sdd = (ShopDailyDiscount) search(groupId,type,itemId);
		if (null != sdd && sdd.getId() == itemId)
			return sdd;
		return null;
	}

	@Override
	public long getSearchKey1(Object obj) {
		return ((ShopDailyDiscount) obj).getGroupId();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return buildKey(((ShopDailyDiscount) obj).getType(),
				((ShopDailyDiscount) obj).getId());
	}

	public Object search(int groupId, byte type, int id) {
		return super.search(groupId, buildKey(type, id));
	}

}
