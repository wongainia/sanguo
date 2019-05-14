package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqEquipmentSell;

public class EquipmentSellReq extends BaseReq {
	private MsgReqEquipmentSell req;

	public EquipmentSellReq(long id) {
		req = new MsgReqEquipmentSell().setId(id);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_EQUIPMENT_SELL
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
