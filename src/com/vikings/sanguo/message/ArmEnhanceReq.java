package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqArmEnhance;

public class ArmEnhanceReq extends BaseReq {

	private MsgReqArmEnhance req;

	public ArmEnhanceReq(int armId, int effectId, int type) {
		req = new MsgReqArmEnhance().setArmid(armId).setEffectid(effectId)
				.setType(type);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_ARM_ENHANCE
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
