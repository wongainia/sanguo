package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqRichBattleInfoQuery;

public class RichBattleInfoQueryReq extends BaseReq {

	private MsgReqRichBattleInfoQuery req;

	public RichBattleInfoQueryReq(long battleId, boolean needHero) {
		req = new MsgReqRichBattleInfoQuery().setBattleId(battleId)
				.setNeedHero(needHero);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_RICH_BATTLE_INFO_QUERY
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
