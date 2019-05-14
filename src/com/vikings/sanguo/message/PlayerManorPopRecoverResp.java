package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.ManorInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspPlayerManorPopRecover;

/**
 * 
 * @author susong
 * 
 */
public class PlayerManorPopRecoverResp extends BaseResp {
	private ReturnInfoClient ri;
	private ManorInfoClient mic;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspPlayerManorPopRecover rsp = new MsgRspPlayerManorPopRecover();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		ri = ReturnInfoClient.convert2Client(rsp.getRi());
		mic = ManorInfoClient.convert(rsp.getManorInfo());
	}

	public ReturnInfoClient getRi() {
		return ri;
	}

	public ManorInfoClient getMic() {
		return mic;
	}
}
