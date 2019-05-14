package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqManorReceive;

public class ManorReceiveReq extends BaseReq {

	private MsgReqManorReceive req;

	public ManorReceiveReq(int buildingId) {
		req = new MsgReqManorReceive();
		req.setBuildingid(buildingId);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_MANOR_RECEIVE
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
