package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqFiefRecommendEmptySpace;

//建筑改造
public class ManorRecommendEmptySpaceReq extends BaseReq {

	private MsgReqFiefRecommendEmptySpace req;

	public ManorRecommendEmptySpaceReq(long pos) {
		req = new MsgReqFiefRecommendEmptySpace().setPos(pos);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_QUERY_EMPTY_ZONE
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
