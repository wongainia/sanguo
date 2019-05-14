package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqConfirmBuyBattleUnit;

public class ConfirmBuyBattleUnitReq extends BaseReq {

	private MsgReqConfirmBuyBattleUnit req;

	public ConfirmBuyBattleUnitReq(long battleid) {
		req = new MsgReqConfirmBuyBattleUnit().setBattleid(battleid);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_BATTLE_CONFIRM_BUY_UNIT
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
