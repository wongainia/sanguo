package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqBattleDivine;

public class ManorWeakRemoveReq extends BaseReq {

	private MsgReqBattleDivine req;

	public ManorWeakRemoveReq() {
		req = new MsgReqBattleDivine();
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_MANOR_WEAK_REMOVE
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
