package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.protos.HonorRankType;
import com.vikings.sanguo.protos.MsgReqHonorRankInfo;

public class HonorRankInfoReq extends BaseReq {
	private MsgReqHonorRankInfo req;

	public HonorRankInfoReq(HonorRankType type, ResultPage resultPage, int guild) {
		req = new MsgReqHonorRankInfo().setType(type)
				.setStart(resultPage.getCurIndex())
				.setCount(Integer.valueOf(resultPage.getPageSize()));
		if (guild > 0)
			req.setGuildid(guild);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_HONOR_RANK_INFO
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}
}
