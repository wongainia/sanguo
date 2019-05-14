package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqFiefReceive;

public class FiefReceiveReq extends BaseReq {

	private MsgReqFiefReceive req;

	public FiefReceiveReq(long fiefId) {
		req = new MsgReqFiefReceive();
		req.setFiefid(fiefId);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_FIEF_RECEIVE
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
