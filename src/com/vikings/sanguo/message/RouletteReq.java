package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqRoulette;

public class RouletteReq extends BaseReq {
	private MsgReqRoulette req;

	public RouletteReq(int type) {
		req = new MsgReqRoulette().setType(type);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_ROULETTE
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}
}
