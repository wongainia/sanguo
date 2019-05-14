package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspPlayerUpdate;

public class PlayerUpdateResp extends BaseResp {
	private ReturnInfoClient ri;

	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspPlayerUpdate resp = new MsgRspPlayerUpdate();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		ri = ReturnInfoClient.convert2Client(resp.getRi());
	}

	public ReturnInfoClient getRi() {
		return ri;
	}

}
