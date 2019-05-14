/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-10-9 下午5:38:27
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspRankReward;

public class HonorRankRewardResp extends BaseResp{
	private ReturnInfoClient ric;
	
	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspRankReward resp = new MsgRspRankReward();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		ric = ReturnInfoClient.convert2Client(resp.getRi());
	}

	public ReturnInfoClient getReturnInfoClient() {
		return ric;
	}
}
