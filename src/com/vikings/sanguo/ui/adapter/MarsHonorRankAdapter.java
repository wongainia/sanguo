/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-10-9 下午2:39:50
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.adapter;

import com.vikings.sanguo.model.HonorRankData;

public class MarsHonorRankAdapter extends HonorRankAdapter{

	@Override
	protected String getDesc(HonorRankData hrd) {
		if (null != hrd && null != hrd.getHonorRank())
			return "昨日击杀" + hrd.getHonorRank().getRankData() + "名敌军";
		else
			return "昨日没有击杀敌军";
	}

}
