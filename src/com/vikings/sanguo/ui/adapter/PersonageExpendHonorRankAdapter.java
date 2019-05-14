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

public class PersonageExpendHonorRankAdapter extends HonorRankAdapter {

	@Override
	protected String getDesc(HonorRankData hrd) {
		if (null != hrd && null != hrd.getHonorRank())
			return "昨日消耗元宝" + hrd.getHonorRank().getRankData();
		else
			return "昨日没有消耗元宝";
	}

}
