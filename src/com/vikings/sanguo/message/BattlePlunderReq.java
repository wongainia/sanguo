package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqPlunderAttack;

public class BattlePlunderReq extends BaseReq {

	private MsgReqPlunderAttack req;

	public BattlePlunderReq(int type, int target, long fiefid) {
		req = new MsgReqPlunderAttack().setType(type).setTarget(target).setFiefid(fiefid);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_PLUNDER_ATTACK.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
