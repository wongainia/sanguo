package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.dyuproject.protostuff.ByteString;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.UserAccountClient;
import com.vikings.sanguo.protos.MsgLoginReq;
import com.vikings.sanguo.utils.BytesUtil;
import com.vikings.sanguo.utils.DateUtil;

public class LoginReq extends BaseReq {

	private MsgLoginReq req;

	public LoginReq(UserAccountClient user) {
		req = new MsgLoginReq().setPsw(user.getPsw())
				.setAesKey(ByteString.copyFrom(Config.clientKey.getEncoded()))
				.setReqid(BytesUtil.getLong(DateUtil.getTimeSS(), seq++));
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_LOGIN.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
