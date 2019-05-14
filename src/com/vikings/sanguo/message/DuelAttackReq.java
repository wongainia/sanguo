package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqDuelAttack;

public class DuelAttackReq extends BaseReq {

	private MsgReqDuelAttack req;

	public DuelAttackReq(int type, int target, long fiefId) {
		req = new MsgReqDuelAttack().setType(type).setTarget(target)
				.setFiefid(fiefId);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_DUEL_ATTACK
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
