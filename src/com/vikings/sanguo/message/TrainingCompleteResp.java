package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspTrainingComplete;

/**
 * 
 * @author susong
 * 
 */
public class TrainingCompleteResp extends BaseResp {
	private ReturnInfoClient ri;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspTrainingComplete resp = new MsgRspTrainingComplete();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		ri = ReturnInfoClient.convert2Client(resp.getInfo());
		Account.manorInfoClient.updateArmFromReturnInfo(ri);
	}

	public ReturnInfoClient getRi() {
		return ri;
	}
}
