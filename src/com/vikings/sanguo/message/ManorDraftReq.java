package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqManorDraft;

public class ManorDraftReq extends BaseReq {

	private MsgReqManorDraft req;

	public ManorDraftReq(int buildingId, int propid, int type, int count) {
		req = new MsgReqManorDraft().setBuildingid(buildingId)
				.setPropid(propid).setType(type).setCount(count);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_MANOR_DRAFT.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
