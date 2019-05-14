package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspGetHelpArm;

public class GetHelpArmResp extends BaseResp {
	private ReturnInfoClient ric;

	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspGetHelpArm resp = new MsgRspGetHelpArm();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		ric = ReturnInfoClient.convert2Client(resp.getRi());
	}

	public ReturnInfoClient getRic() {
		return ric;
	}
}
