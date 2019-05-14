package com.vikings.sanguo.message;

import java.io.OutputStream;

public class PlayerManorPopRecoverReq extends BaseReq {

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_MANOR_POP_RECOVER.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
	}

}
