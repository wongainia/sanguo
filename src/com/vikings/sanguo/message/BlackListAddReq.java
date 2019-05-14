package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgBlacklistAddReq;

public class BlackListAddReq extends BaseReq {

	private MsgBlacklistAddReq req;

	public BlackListAddReq(int targetId) {
		req = new MsgBlacklistAddReq().setTargetid(targetId);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_BLACKLIST_ADD.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
