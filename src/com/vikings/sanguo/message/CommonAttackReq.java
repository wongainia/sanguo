package com.vikings.sanguo.message;

import java.io.OutputStream;
import com.vikings.sanguo.protos.MsgReqCommonAttack;

public class CommonAttackReq extends BaseReq {

	private MsgReqCommonAttack req;

	public CommonAttackReq(int type, int target, long fiefid) {
		req = new MsgReqCommonAttack().setType(type).setTarget(target).setFiefid(fiefid);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_BATTLE_ATTACK.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
