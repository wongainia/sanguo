package com.vikings.sanguo.message;

import java.io.OutputStream;

public class GetHelpArmReq extends BaseReq {

	@Override
	protected void toBytes(OutputStream out) {

	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_GET_HELP_ARM
				.getNumber();
	}

}
