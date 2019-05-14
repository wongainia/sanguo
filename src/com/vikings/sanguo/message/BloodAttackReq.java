package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqBloodAttack;

public class BloodAttackReq extends BaseReq {

	private MsgReqBloodAttack req;

	public BloodAttackReq(int num) {
		req = new MsgReqBloodAttack().setNum(num);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_BLOOD_ATTACK
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
