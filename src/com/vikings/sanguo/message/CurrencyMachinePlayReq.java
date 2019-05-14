package com.vikings.sanguo.message;

import java.io.OutputStream;

public class CurrencyMachinePlayReq extends BaseReq {

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_CURRENCY_MACHINE_PLAY
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
	}
}
