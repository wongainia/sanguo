/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-12-3 下午6:15:26
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.LordInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspArenaReward;

public class ArenaRewardResp extends BaseResp{
	private ReturnInfoClient ric;
	
	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspArenaReward resp = new MsgRspArenaReward();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		ric = ReturnInfoClient.convert2Client(resp.getRi());
		Account.myLordInfo = LordInfoClient.convert(resp.getLi());
	}

	public ReturnInfoClient getReturnInfoClient() {
		return ric;
	}
}
