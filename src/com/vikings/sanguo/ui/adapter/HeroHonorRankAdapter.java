/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-10-9 下午4:02:10
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.adapter;

import com.vikings.sanguo.model.HonorRankData;
import com.vikings.sanguo.utils.StringUtil;

public class HeroHonorRankAdapter extends HonorRankAdapter {

	@Override
	protected String getDesc(HonorRankData hrd) {
		if (hasHero(hrd))
			return StringUtil.getHeroTypeName(hrd.getHeroQuality())
					+ StringUtil.getHeroName(hrd.getHeroProp(),
							hrd.getHeroQuality()) + "提升了"
					+ hrd.getHonorRank().getRankData() + "统率";
		else
			return "";
	}

	private boolean hasHero(HonorRankData hrd) {
		return null != hrd && null != hrd.getHonorRank()
				&& null != hrd.getHeroProp() && null != hrd.getHeroQuality()
				&& hrd.getHeroId() > 0;
	}
}
