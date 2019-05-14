package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqEquipmentItemInsert;

public class EquipmentItemInsertReq extends BaseReq {
	private MsgReqEquipmentItemInsert req;

	public EquipmentItemInsertReq(long id, long hero, int itemId) {
		req = new MsgReqEquipmentItemInsert().setId(id).setItemid(itemId);
		if (hero > 0)
			req.setHero(hero);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_EQUIPMENT_ITEM_INSERT
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
