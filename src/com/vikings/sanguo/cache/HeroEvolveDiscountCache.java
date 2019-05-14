/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-9-4 下午4:38:02
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.HeroEvolveDiscount;

public class HeroEvolveDiscountCache extends ArrayFileCache{
	private static final String FILE_NAME = "hero_evolve_discount.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public long getSearchKey1(Object obj) {
		return ((HeroEvolveDiscount)obj).getMin();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return ((HeroEvolveDiscount)obj).getMax();
	}

	@Override
	public Object fromString(String line) {
		return HeroEvolveDiscount.fromString(line);
	}

	public int getDiscount(int cnt) {
		HeroEvolveDiscount hed = null;
		for (Object it : list) {
			HeroEvolveDiscount tmp = (HeroEvolveDiscount) it;
			if (cnt >= tmp.getMin() && cnt <= tmp.getMax()) {
				hed = tmp;
				break;
			}
		}
		
		return null == hed ? 100 : hed.getPercent();
	}
}
