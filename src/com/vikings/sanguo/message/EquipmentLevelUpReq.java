package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqEquipmentLevelup;

public class EquipmentLevelUpReq extends BaseReq {
	private MsgReqEquipmentLevelup req;

	public EquipmentLevelUpReq(long id, long heroId, int type) {
		req = new MsgReqEquipmentLevelup().setId(id).setType(type);
		if (heroId > 0)
			req.setHero(heroId);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_EQUIPMENT_LEVELUP
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
