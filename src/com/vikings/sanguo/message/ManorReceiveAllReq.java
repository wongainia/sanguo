package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqManorReceiveAll;

public class ManorReceiveAllReq extends BaseReq {

	private MsgReqManorReceiveAll req;
	
	
	public ManorReceiveAllReq() {
		req = new MsgReqManorReceiveAll().setReceiveFief(false);
	}
	
	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_MANOR_RECEIVE_ALL
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
