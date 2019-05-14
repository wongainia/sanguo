package com.vikings.sanguo.message;

import java.io.OutputStream;

public class LogoutReq extends BaseReq {

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_LOGOUT.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {

	}

}
