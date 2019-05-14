package com.vikings.sanguo.message;

import java.io.OutputStream;

public class UserTroopCostReq extends BaseReq {

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_USER_TROOP_COST_UPDATE
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
	}

}
