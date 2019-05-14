package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqHeroRefresh;

public class HeroRefreshReq extends BaseReq {

	private MsgReqHeroRefresh req;

	public HeroRefreshReq(int type) {
		req = new MsgReqHeroRefresh().setRefreshType(type);
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_HERO_REFRESH
				.getNumber();
	}
}
