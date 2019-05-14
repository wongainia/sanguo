package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgQueryRichOtherUserInfoReq;

public class QueryRichOtherUserInfoReq extends BaseReq {

	private MsgQueryRichOtherUserInfoReq req;

	public QueryRichOtherUserInfoReq(int target, long flag) {
		req = new MsgQueryRichOtherUserInfoReq().setTarget(target)
				.setFlag(flag);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_RICH_OTHER_USER_INFO_QUERY.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
