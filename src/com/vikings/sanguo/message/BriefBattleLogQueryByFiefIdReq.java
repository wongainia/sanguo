package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqBriefBattleLogQueryByFiefId;

public class BriefBattleLogQueryByFiefIdReq extends BaseReq {

	private MsgReqBriefBattleLogQueryByFiefId req;

	public BriefBattleLogQueryByFiefIdReq(long fiefid, int start, int count) {
		req = new MsgReqBriefBattleLogQueryByFiefId().setFiefid(fiefid)
				.setStart(start).setCount(count);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_BRIEF_BATTLE_INFO_QUERY_BY_FIEFID.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
