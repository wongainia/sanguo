package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.dyuproject.protostuff.ByteString;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.protos.MsgReqAccountQuery;

public class AccountQueryReq extends BaseReq {
	private MsgReqAccountQuery req;

	public AccountQueryReq(String imsi) {
		req = new MsgReqAccountQuery();
		req.setSim(imsi).setKey(
				ByteString.copyFrom(Config.clientKey.getEncoded()));
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_ACCOUNT_QUERY
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
