package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspUserStaminaReset;

public class UserStaminaResetResp extends BaseResp {
	private ReturnInfoClient ric;

	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspUserStaminaReset rsp = new MsgRspUserStaminaReset();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		ric = ReturnInfoClient.convert2Client(rsp.getRi());
	}

	public ReturnInfoClient getRic() {
		return ric;
	}
}
