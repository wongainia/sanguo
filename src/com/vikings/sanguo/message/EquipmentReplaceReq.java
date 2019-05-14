package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqEquipmentReplace;

public class EquipmentReplaceReq extends BaseReq {
	private MsgReqEquipmentReplace req;

	public EquipmentReplaceReq(long id, long srcHero, long targetHero) {
		req = new MsgReqEquipmentReplace().setId(id).setTargetHero(targetHero);
		if (srcHero > 0)
			req.setSrcHero(srcHero);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_EQUIPMENT_REPLACE
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
