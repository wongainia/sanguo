package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqEquipmentBuy;

public class EquipmentBuyReq extends BaseReq {
	private MsgReqEquipmentBuy req;

	public EquipmentBuyReq(int scheme) {
		req = new MsgReqEquipmentBuy().setScheme(scheme);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_EQUIPMENT_BUY
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
