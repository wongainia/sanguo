package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqBloodPoker;

public class BloodPokerReq extends BaseReq {

	private MsgReqBloodPoker req;

	public BloodPokerReq(int pos) {
		req = new MsgReqBloodPoker().setPos(pos);
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_BLOOD_POKER
				.getNumber();
	}
}
