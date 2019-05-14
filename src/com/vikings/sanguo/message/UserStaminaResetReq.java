package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqUserStaminaReset;

public class UserStaminaResetReq extends BaseReq {
	private MsgReqUserStaminaReset req;

	public UserStaminaResetReq(int currency) {
		req = new MsgReqUserStaminaReset().setCurrency(currency);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_USER_STAMINA_RESET
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
