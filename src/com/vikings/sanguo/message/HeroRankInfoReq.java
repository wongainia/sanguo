/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-10-11 上午10:50:42
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.protos.MsgReqHeroRankInfo;

public class HeroRankInfoReq extends BaseReq{
	private MsgReqHeroRankInfo req;
	
	public HeroRankInfoReq(ResultPage resultPage) {
		req = new MsgReqHeroRankInfo().setStart(resultPage.getCurIndex())
				.setCount(Integer.valueOf(resultPage.getPageSize()));
	}
	
	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_HERO_RANK_INFO.getNumber();
	}

}
