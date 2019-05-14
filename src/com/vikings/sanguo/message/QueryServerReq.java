package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.access.PrefAccess;
import com.vikings.sanguo.config.Config;

public class QueryServerReq extends BaseReq {

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_SERVER_WHERE
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
	}

	@Override
	protected int getUserId() {
		if (Config.getController() != null)
			return Config.getAccountClient().getId();
		else
			return 0;
	}

}
