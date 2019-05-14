/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-12-17 上午11:53:42
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.cache;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.model.BuildingProp;
import com.vikings.sanguo.model.ManorRevive;

public class ManorReviveCache extends LazyLoadArrayCache {

	public static final int TYPE_ISARM = 0;// 士兵
	public static final int TYPE_ISBOSS = 1;// boss

	@Override
	public long getSearchKey1(Object obj) {
		return ((ManorRevive) obj).getArmPropId();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return ((ManorRevive) obj).getArmPropId();
	}

	@Override
	public Object fromString(String line) {
		return ManorRevive.fromString(line);
	}

	@Override
	public String getName() {
		return "manor_revive.csv";
	}

	public ManorRevive getManorRevive(int lvl, int armId) {
		checkLoad();
		for (Object it : list) {
			ManorRevive mr = (ManorRevive) it;
			if (lvl >= mr.getMinLvl() && lvl <= mr.getMaxLvl()
					&& mr.getArmPropId() == armId)
				return mr;
		}
		return null;
	}

	// boss按照元宝消耗救治
	public float getBossReviveCost(int lvl, ArmInfoClient it, int count) {
		ManorRevive mr = getManorRevive(lvl, it.getId());
		if (null == mr)
			return 0;
		return count * mr.getRmb() / 10000f;
	}

	// 普兵消耗金币根据次数有增幅 count复活次数
	public float getArmReviveCose(int lvl, ArmInfoClient it, int count,
			int armCount) {
		ManorRevive mr = getManorRevive(lvl, it.getId());
		if (null == mr)
			return 0;
		return armCount
				* ((mr.getGold() + count * mr.getSecondGold()) / 10000f);
	}

	public int getReviveCD(BuildingProp bp) {
		int reviveStart = Account.manorInfoClient.getManorReviveCDStart(bp
				.getId());
		if (0 == reviveStart)
			return 0;
		return reviveStart - Config.serverTimeSS();
	}

}
