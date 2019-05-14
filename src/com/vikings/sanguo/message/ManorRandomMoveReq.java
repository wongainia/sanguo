package com.vikings.sanguo.message;

import java.io.OutputStream;

public class ManorRandomMoveReq extends BaseReq {

	public ManorRandomMoveReq() {

	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_MANOR_RANDOM_MOVE
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {

	}

}
