package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqMessagePost;

public class MessagePostReq extends BaseReq {

	private MsgReqMessagePost req;

	public MessagePostReq(int type, int target, String context) {
		req = new MsgReqMessagePost().setType(type).setContext(context);
		if (target > 0)
			req.setTarget(target);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_MESSAGE_POST
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
