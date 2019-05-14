/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-10-9 上午10:46:48
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.protos.MsgRspHonorRankInfo;

public class HonorRankInfoResp extends BaseResp{
	private MsgRspHonorRankInfo rsp;
	
	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		rsp = new MsgRspHonorRankInfo();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
	}

	public MsgRspHonorRankInfo getMsgRspHonorRankInfo() {
		return rsp;
	}
}
