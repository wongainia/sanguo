/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-10-11 上午10:53:17
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.protos.MsgRspHeroRankInfo;

public class HeroRankInfoResp extends BaseResp{
	private MsgRspHeroRankInfo rsp;
	
	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		rsp = new MsgRspHeroRankInfo();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
	}

	public MsgRspHeroRankInfo getMsgRspHeroRankInfo() {
		return rsp;
	}
}
