package com.vikings.sanguo.message;

import java.io.OutputStream;

public class ManorReviveCleanReq extends BaseReq {

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_MANOR_REVIVE_CLEAN
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
	}

}
