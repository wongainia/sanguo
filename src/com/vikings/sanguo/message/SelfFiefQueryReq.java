package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqSelfFiefInfoQuery;

public class SelfFiefQueryReq extends BaseReq {

	private MsgReqSelfFiefInfoQuery req;

	public SelfFiefQueryReq(long fiefId) {
		req = new MsgReqSelfFiefInfoQuery().setFiefid(fiefId);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_SELF_FIEF_INFO_QUERY
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
