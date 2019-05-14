/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-10-9 下午4:01:05
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.adapter;

import com.vikings.sanguo.model.HonorRankData;

public class GuildHonorRankAdapter extends HonorRankAdapter{

	@Override
	protected String getDesc(HonorRankData hrd) {
		if (null != hrd && null != hrd.getHonorRank())
			return "家族昨日击杀" + hrd.getHonorRank().getRankData() + "名敌军";
		else
			return "";
	}

}
