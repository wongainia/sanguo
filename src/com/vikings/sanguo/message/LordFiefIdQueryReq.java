package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqLordFiefIdQuery;

public class LordFiefIdQueryReq extends BaseReq {

	private MsgReqLordFiefIdQuery req;

	public LordFiefIdQueryReq(int target) {
		req = new MsgReqLordFiefIdQuery().setTarget(target);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_LORD_FIEFID_QUERY.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
