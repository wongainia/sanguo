package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.protos.MsgReqHotUserAttrScoreInfoQuery;

public class HotUserAttrScoreInfoQueryReq extends BaseReq {
	private MsgReqHotUserAttrScoreInfoQuery req;

	public HotUserAttrScoreInfoQueryReq(int level, int country,
			ResultPage resultPage, int type) {
		req = new MsgReqHotUserAttrScoreInfoQuery().setCountry(country)
				.setLevel(level).setStart(resultPage.getCurIndex())
				.setType(type);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_HOT_USER_SCORE_INFO_QUERY
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
