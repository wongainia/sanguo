/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-10-11 上午10:35:15
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.protos.MsgRspUserRankInfo;

public class UserRankInfoResp extends BaseResp{
	private MsgRspUserRankInfo rsp;
	
	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		rsp = new MsgRspUserRankInfo();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
	}

	public MsgRspUserRankInfo getMsgRspUserRankInfo() {
		return rsp;
	}
}
