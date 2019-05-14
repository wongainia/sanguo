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

import com.vikings.sanguo.model.ArmEnhanceCost;

public class ArmEnhanceCostCache extends ArrayFileCache {
	public static String FILE_NAME = "arm_enhance_cost.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object fromString(String line) {
		return ArmEnhanceCost.fromString(line);
	}

	@Override
	public long getSearchKey1(Object obj) {
		return ((ArmEnhanceCost) obj).getArmId();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return buildKey(((ArmEnhanceCost) obj).getPropId(),
				((ArmEnhanceCost) obj).getLevel());
	}

	public int getCostRmb(int armId, int propId, int level) {
		int cost = 0;
		Object obj = search(armId, buildKey(propId, level));
		if (null != obj) {
			cost = ((ArmEnhanceCost) obj).getCostRmb();
		}
		return cost;
	}
}
