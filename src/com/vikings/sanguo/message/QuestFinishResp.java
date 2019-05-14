package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspQuestFinish;

/**
 * 
 * @author susong
 * 
 */
public class QuestFinishResp extends BaseResp {
	private ReturnInfoClient ri;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspQuestFinish rsp = new MsgRspQuestFinish();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		ri = ReturnInfoClient.convert2Client(rsp.getRi());
		Account.manorInfoClient.updateArmFromReturnInfo(ri);
	}

	public ReturnInfoClient getRi() {
		return ri;
	}
}
