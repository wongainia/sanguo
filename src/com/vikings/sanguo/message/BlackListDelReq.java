package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgBlacklistDelReq;

public class BlackListDelReq extends BaseReq {

	private MsgBlacklistDelReq req;

	public BlackListDelReq(int targetId) {
		req = new  MsgBlacklistDelReq().setTargetid(targetId);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_BLACKLIST_DEL.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
