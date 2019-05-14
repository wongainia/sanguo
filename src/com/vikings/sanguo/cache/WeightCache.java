/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-5-4 下午5:11:47
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.ItemWeight;

public class WeightCache extends FileCache {

	public final static String FILE_NAME = "prop_weight.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((ItemWeight) obj).getId();
	}

	@Override
	public Object fromString(String line) {
		return ItemWeight.fromString(line);
	}

	public int getWeight(int attrId) {
		if (!content.containsKey(attrId))
			return 0;
		return ((ItemWeight) content.get(attrId)).getWeight();
	}

}
