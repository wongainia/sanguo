package com.vikings.sanguo.message;

import java.io.OutputStream;
import java.util.List;

import com.vikings.sanguo.protos.MsgReqBriefFiefInfoQuery;

public class BriefFiefInfoQueryReq extends BaseReq {

	private MsgReqBriefFiefInfoQuery req;

	public BriefFiefInfoQueryReq(List<Long> fiefids) {
		req = new MsgReqBriefFiefInfoQuery().setFiefidsList(fiefids);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_FIEF_QUERY_BRIEF
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
