package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqEquipmentDisarm;

public class EquipmentDisarmReq extends BaseReq {
	private MsgReqEquipmentDisarm req;

	public EquipmentDisarmReq(long id, long targetHero) {
		req = new MsgReqEquipmentDisarm().setId(id).setTargetHero(targetHero);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_EQUIPMENT_DISARM
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
