package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.LordInfoClient;
import com.vikings.sanguo.protos.MsgRspManorReviveClean;

public class ManorReviveCleanResp extends BaseResp {

	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspManorReviveClean rsp = new MsgRspManorReviveClean();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		Account.myLordInfo = LordInfoClient.convert(rsp.getLi());
	}
}
