package com.vikings.sanguo.message;

import java.io.OutputStream;

public class ChargeMonthRewardReq extends BaseReq {

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_CHARGE_MONTH_REWARD
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
	}
}
