package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqHeroExchange;

public class HeroExchangeReq extends BaseReq {

	private MsgReqHeroExchange req;

	public HeroExchangeReq(int id) {
		req = new MsgReqHeroExchange().setId(id);
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_HERO_EXCHANGE
				.getNumber();
	}
}
