package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.dyuproject.protostuff.ByteString;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.protos.MsgReqAccountRestore;

public class AccountRestoreReq extends BaseReq {

	private MsgReqAccountRestore req;

	/**
	 * 获取验证码
	 * 
	 * @param flag
	 *            (1：手机，2：email)
	 * @param email
	 */
	public AccountRestoreReq(int flag, String value) {
		req = new MsgReqAccountRestore().setFlag(flag).setValue(value)
				.setKey(ByteString.copyFrom(Config.clientKey.getEncoded()));
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_ACCOUNT_RESTORE
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
