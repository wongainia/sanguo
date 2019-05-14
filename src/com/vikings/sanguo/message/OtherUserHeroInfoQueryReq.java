package com.vikings.sanguo.message;

import java.io.OutputStream;
import java.util.List;

import com.vikings.sanguo.protos.MsgReqOtherUserHeroInfoQuery;

public class OtherUserHeroInfoQueryReq extends BaseReq {
	private MsgReqOtherUserHeroInfoQuery req;

	public OtherUserHeroInfoQueryReq(int userId, List<Long> ids) {
		req = new MsgReqOtherUserHeroInfoQuery().setTarget(userId);
		if (ids != null && !ids.isEmpty())
			req.setIdsList(ids);
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_OTHER_USER_HERO_INFO_QUERY.getNumber();
	}

}
