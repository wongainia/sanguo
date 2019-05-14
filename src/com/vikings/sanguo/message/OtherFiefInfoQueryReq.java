package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqOtherFiefInfoQuery;

public class OtherFiefInfoQueryReq extends BaseReq {

	private MsgReqOtherFiefInfoQuery req;

	public OtherFiefInfoQueryReq(long fiefid) {
		req = new MsgReqOtherFiefInfoQuery().setFiefid(fiefid);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_OTHER_FIEF_INFO_QUERY.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
