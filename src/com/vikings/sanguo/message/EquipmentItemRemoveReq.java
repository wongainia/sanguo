package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqEquipmentItemRemove;

public class EquipmentItemRemoveReq extends BaseReq {
	private MsgReqEquipmentItemRemove req;

	public EquipmentItemRemoveReq(long id, long hero) {
		req = new MsgReqEquipmentItemRemove().setId(id);
		if (hero > 0)
			req.setHero(hero);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_EQUIPMENT_ITEM_REMOVE
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
