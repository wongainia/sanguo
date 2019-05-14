package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqEquipmentForge;

public class EquipmentForgeReq extends BaseReq {
	private MsgReqEquipmentForge req;

	public EquipmentForgeReq(long id, long hero, int effectType,
			boolean useCurrency) {
		req = new MsgReqEquipmentForge().setId(id).setEffectType(effectType)
				.setUseCurrency(useCurrency);
		if (hero > 0)
			req.setHero(hero);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_EQUIPMENT_FORGE
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
