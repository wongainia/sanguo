package com.vikings.sanguo.message;

import java.io.OutputStream;

public class UserCheckInReq extends BaseReq {


	public UserCheckInReq() {
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_USER_CHECKIN
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
	}

}
