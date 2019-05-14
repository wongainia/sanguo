package com.vikings.sanguo.message;

import java.io.OutputStream;
import java.util.List;

import com.vikings.sanguo.protos.MsgReqBriefGuildInfoQuery;

public class BriefGuildInfoQueryReq extends BaseReq {

	private MsgReqBriefGuildInfoQuery req;

	public BriefGuildInfoQueryReq(List<Integer> ids) {
		req = new MsgReqBriefGuildInfoQuery().setIdsList(ids);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_BRIEF_GUILD_INFO_QUERY.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
