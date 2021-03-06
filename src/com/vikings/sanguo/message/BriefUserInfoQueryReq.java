package com.vikings.sanguo.message;

import java.io.OutputStream;
import java.util.List;

import com.vikings.sanguo.protos.MsgReqBriefUserInfoQuery;

public class BriefUserInfoQueryReq extends BaseReq {

	private MsgReqBriefUserInfoQuery req;

	public BriefUserInfoQueryReq(List<Integer> ids) {
		req = new MsgReqBriefUserInfoQuery().setUseridsList(ids);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_BRIEF_USER_INFO_QUERY.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
