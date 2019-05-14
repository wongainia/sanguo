package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqBuildingReset;

public class BuildingResetReq extends BaseReq {

	private MsgReqBuildingReset req;

	public BuildingResetReq(int buildingId, long fiefId) {
		req = new MsgReqBuildingReset().setBuildingid(buildingId).setFiefid(
				fiefId);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_BUILDING_RESET
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
