package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.dyuproject.protostuff.ByteString;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.protos.MsgReqAccountRestore2;

public class AccountRestore2Req extends BaseReq {

	private MsgReqAccountRestore2 req;

	public AccountRestore2Req(int flag, String value, int code) {
		req = new MsgReqAccountRestore2().setFlag(flag).setValue(value)
				.setKey(ByteString.copyFrom(Config.clientKey.getEncoded()));
		if (code > 0)
			req.setCode(code);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_ACCOUNT_RESTORE2
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
