package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqBuyBattleUnit;

public class BuyBattleUnitReq extends BaseReq {

	private MsgReqBuyBattleUnit req;

	public BuyBattleUnitReq(long battleId, int index) {
		req = new MsgReqBuyBattleUnit().setBattleid(battleId).setIndex(index);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_BATTLE_BUY_UNIT.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
