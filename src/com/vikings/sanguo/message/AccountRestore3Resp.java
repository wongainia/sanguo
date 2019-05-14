package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.protos.MsgRspAccountRestore3;

public class AccountRestore3Resp extends BaseResp {
	private String psw;

	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspAccountRestore3 rsp = new MsgRspAccountRestore3();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		psw = rsp.getPsw();
	}

	public String getPsw() {
		return psw;
	}

}
