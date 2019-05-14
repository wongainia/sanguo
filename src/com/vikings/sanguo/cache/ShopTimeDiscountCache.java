package com.vikings.sanguo.cache;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.model.ShopTimeDiscount;

public class ShopTimeDiscountCache extends ArrayFileCache {
	private final static String NAME = "shop_time_discount.csv";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public long getSearchKey1(Object obj) {
		return ((ShopTimeDiscount) obj).getType();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return ((ShopTimeDiscount) obj).getId();
	}

	@Override
	public Object fromString(String line) {
		return ShopTimeDiscount.fromString(line);
	}

	//折扣率
	public int getDiscount(byte type, int id) {
		if (null!=(ShopTimeDiscount) search(type, id)) {
			ShopTimeDiscount std = (ShopTimeDiscount) search(type, id);
			if (std != null
					&& CacheMgr.timeTypeConditionCache.isWithinTime(std
							.getTimeConditionId()))
				return std.getDiscount();
		}
		return Constants.NO_DISCOUNT;
	}

	//折扣时间
	public int getCountDownSecond(byte type,int id) {
		if (null!=(ShopTimeDiscount) search(type, id)) {
			ShopTimeDiscount std = (ShopTimeDiscount) search(type, id);
			if(null!=std){
				return CacheMgr.timeTypeConditionCache.getCountDownSecond(std
						.getTimeConditionId());
			}
		}
		return 0;
	}

}
