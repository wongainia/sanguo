package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.protos.MsgReqBloodRankQuery;

public class BloodRankQueryReq extends BaseReq {
	private MsgReqBloodRankQuery req;

	public BloodRankQueryReq(ResultPage resultPage, boolean self) {
		req = new MsgReqBloodRankQuery().setStart(resultPage.getCurIndex())
				.setCount(Integer.valueOf(resultPage.getPageSize()))
				.setSelf(self);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_BLOOD_RANK_QUERY
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}
}
