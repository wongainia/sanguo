package com.vikings.sanguo.message;

import java.io.OutputStream;

public class MachinePlayStatDataReq extends BaseReq {

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_MACHINE_PLAY_STAT_DATA
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
	}
}
