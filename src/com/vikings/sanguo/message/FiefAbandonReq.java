package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqFiefAbandon;

public class FiefAbandonReq extends BaseReq {

	private MsgReqFiefAbandon req;

	public FiefAbandonReq(long fiefid) {

		req = new MsgReqFiefAbandon().setFiefid(fiefid);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_FIEF_ABANDON.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
