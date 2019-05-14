package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqOtherLordTroopInfoQuery;

public class OtherLordTroopInfoQueryReq extends BaseReq {
	private MsgReqOtherLordTroopInfoQuery req;

	public OtherLordTroopInfoQueryReq(int target) {
		req = new MsgReqOtherLordTroopInfoQuery().setTarget(target);
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_OTHER_LORD_TROOP_INFO_QUERY.getNumber();
	}

}
