package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.BindingType;
import com.vikings.sanguo.protos.MsgReqAccountBinding2;

public class AccountBinding2Req extends BaseReq {

	private MsgReqAccountBinding2 req;

	public AccountBinding2Req(BindingType type, String value, int code,
			int inviter) {
		req = new MsgReqAccountBinding2()
				.setBindingType(type).setValue(value).setCode(code);
		if (code != 0)
			req.setCode(code);
		if (inviter != 0)
			req.setInviter(inviter);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_ACCOUNT_BIND2.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
