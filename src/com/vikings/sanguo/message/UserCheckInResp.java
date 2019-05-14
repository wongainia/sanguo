package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspUserCheckIn;

/**
 * 
 * @author Brad.Chen
 * 
 */
public class UserCheckInResp extends BaseResp {
	private ReturnInfoClient ri;

	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspUserCheckIn  resp = new MsgRspUserCheckIn ();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		ri = ReturnInfoClient.convert2Client(resp.getRi());
		Account.manorInfoClient.updateArmFromReturnInfo(ri);
	}

	public ReturnInfoClient getRi() {
		return ri;
	}

}
