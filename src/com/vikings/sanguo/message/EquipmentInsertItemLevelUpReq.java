package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqEquipmentInsertItemLevelup;

public class EquipmentInsertItemLevelUpReq extends BaseReq {
	private MsgReqEquipmentInsertItemLevelup req;

	public EquipmentInsertItemLevelUpReq(long id, long heroId) {
		req = new MsgReqEquipmentInsertItemLevelup().setId(id);
		if (heroId > 0)
			req.setHero(heroId);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_EQUIPMENT_INSERT_ITEM_LEVELUP
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
