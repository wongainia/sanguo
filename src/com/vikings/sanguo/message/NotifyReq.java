package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.utils.BytesUtil;

public class NotifyReq extends BaseReq {
	private String password;

	public NotifyReq(String password) {
		this.password = password;
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_TRAY_DATA_SYN.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		BytesUtil.putString(out, password, Constants.MAX_LEN_PASSWORD);
	}

}
