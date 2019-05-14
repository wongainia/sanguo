package com.vikings.sanguo.message;

import java.io.OutputStream;

public class BattleHotInfoReq extends BaseReq {


	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_BATTLE_HOT_INFO.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
	}

}
