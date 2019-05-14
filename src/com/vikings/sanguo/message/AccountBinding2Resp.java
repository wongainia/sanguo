package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspAccountBinding2;

/**
 * 
 * @author susong
 * 
 */
public class AccountBinding2Resp extends BaseResp {
	private int inveter;
	private ReturnInfoClient ri;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspAccountBinding2 rsp = new MsgRspAccountBinding2();
		ProtobufIOUtil.mergeFrom(buf, rsp, MsgRspAccountBinding2.getSchema());
		inveter = rsp.getInviter();
		ri = ReturnInfoClient.convert2Client(rsp.getRi());
	}

	public int getInveter() {
		return inveter;
	}

	public ReturnInfoClient getRi() {
		return ri;
	}
}
