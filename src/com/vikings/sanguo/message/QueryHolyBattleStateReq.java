package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqQueryHolyBattleState;

public class QueryHolyBattleStateReq extends BaseReq {

	private MsgReqQueryHolyBattleState req;

	public QueryHolyBattleStateReq(int country) {
		req = new MsgReqQueryHolyBattleState().setCountry(country);
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_QUERY_HOLY_BATTLE_STATE
				.getNumber();
	}
}
