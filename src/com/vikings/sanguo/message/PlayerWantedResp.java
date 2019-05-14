package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspPlayerWanted;

/**
 * 
 * @author susong
 * 
 */
public class PlayerWantedResp extends BaseResp {
	private ReturnInfoClient ri;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspPlayerWanted rsp = new MsgRspPlayerWanted();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		ri = ReturnInfoClient.convert2Client(rsp.getRi());
	}

	public ReturnInfoClient getRi() {
		return ri;
	}
}
