package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqBattleDivine;

public class BattleDivineReq extends BaseReq {

	private MsgReqBattleDivine req;

	public BattleDivineReq(long fiefid, int type) {
		req = new MsgReqBattleDivine().setFiefid(fiefid).setType(type);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_BATTLE_DIVINE.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
