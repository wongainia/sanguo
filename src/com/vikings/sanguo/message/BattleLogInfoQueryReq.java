package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqBattleLogInfoQuery;

public class BattleLogInfoQueryReq extends BaseReq {

	private MsgReqBattleLogInfoQuery req;

	public BattleLogInfoQueryReq(long battleLogId) {
		req = new MsgReqBattleLogInfoQuery().setId(battleLogId);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_BATTLE_LOG_INFO_QUERY.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
