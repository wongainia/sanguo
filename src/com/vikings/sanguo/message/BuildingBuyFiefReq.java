package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqFiefBuildingBuy;

public class BuildingBuyFiefReq extends BaseReq {

	private MsgReqFiefBuildingBuy req;

	public BuildingBuyFiefReq(int type, int itemId,long fiefId) {
		req = new MsgReqFiefBuildingBuy();
		req.setType(type).setItemid(itemId).setFiefid(fiefId);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_FIEF_BUILDING_BUY.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
