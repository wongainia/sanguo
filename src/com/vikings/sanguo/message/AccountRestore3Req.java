package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.dyuproject.protostuff.ByteString;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.protos.MsgReqAccountRestore3;

public class AccountRestore3Req extends BaseReq {

	private MsgReqAccountRestore3 req;

	public AccountRestore3Req(int userId, String psw, String sim) {
		req = new MsgReqAccountRestore3().setUserid(userId).setPsw(psw)
				.setSim(sim)
				.setKey(ByteString.copyFrom(Config.clientKey.getEncoded()));
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_ACCOUNT_RESTORE3
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
