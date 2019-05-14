package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.LordInfoClient;
import com.vikings.sanguo.protos.MsgRspArenaConf;

public class ArenaConfResp extends BaseResp{

	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspArenaConf rsp = new MsgRspArenaConf();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		Account.myLordInfo = LordInfoClient.convert(rsp.getLi());
	}


}
