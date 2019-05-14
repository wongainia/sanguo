package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqCurrencyBoxOpen;

public class CurrencyBoxOpenReq extends BaseReq {
	private MsgReqCurrencyBoxOpen req;

	public CurrencyBoxOpenReq(int target, int type) {
		req = new MsgReqCurrencyBoxOpen().setTarget(target).setType(type);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_CURRENCY_BOX_OPEN
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}
}
