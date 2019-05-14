package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqManorBuildingBuy;

public class BuildingBuyReq extends BaseReq {

	private MsgReqManorBuildingBuy req;

	public BuildingBuyReq(int type, int itemId) {
		req = new MsgReqManorBuildingBuy();
		req.setType(type).setItemid(itemId);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_MANOR_BUILDING_BUY.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
