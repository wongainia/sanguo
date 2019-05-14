package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqManorLayDown;

public class ManorLayDownReq extends BaseReq {

	private MsgReqManorLayDown req;

	public ManorLayDownReq(long zoneid) {
		req = new MsgReqManorLayDown();
		req.setZoneid(zoneid);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_MANOR_LAY_DOWN
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
