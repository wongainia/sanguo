package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgCheckIfInTargetBlackListReq;

public class BlackListCheckReq extends BaseReq {

	private MsgCheckIfInTargetBlackListReq req;

	public BlackListCheckReq(int target) {
		req = new MsgCheckIfInTargetBlackListReq().setTargetid(target);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_BLACKLIST_CHECK.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
