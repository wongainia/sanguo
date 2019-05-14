package com.vikings.sanguo.message;

import java.io.OutputStream;
import java.util.List;

import com.vikings.sanguo.protos.MsgReqHeroAbandon;

public class HeroAbandonReq extends BaseReq {
	private MsgReqHeroAbandon req;

	public HeroAbandonReq(List<Long> heros) {
		req = new MsgReqHeroAbandon();
		if (null != heros && !heros.isEmpty())
			req.setHerosList(heros);
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_HERO_ABANDON
				.getNumber();
	}

}
