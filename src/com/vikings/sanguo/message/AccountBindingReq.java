package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.BindingType;
import com.vikings.sanguo.protos.MsgReqAccountBinding;

public class AccountBindingReq extends BaseReq {

	private MsgReqAccountBinding req;

	public AccountBindingReq(BindingType type, String value) {
		req = new MsgReqAccountBinding()
				.setBindingType(type).setValue(value);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_ACCOUNT_BIND.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
