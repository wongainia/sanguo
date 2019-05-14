/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-12-16 下午4:57:55
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.LordInfoClient;
import com.vikings.sanguo.model.ManorInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspManorRevive;

public class ManorReviveResp extends BaseResp{
	private ReturnInfoClient ric;
	
	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspManorRevive resp = new MsgRspManorRevive();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		ric = ReturnInfoClient.convert2Client(resp.getRi());
		Account.manorInfoClient.update(ManorInfoClient.convert(resp.getMi()));
		Account.myLordInfo = LordInfoClient.convert(resp.getLi());
	}

	public ReturnInfoClient getRic() {
		return ric;
	}
}
