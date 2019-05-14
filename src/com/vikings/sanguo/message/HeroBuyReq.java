package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqHeroBuy;

public class HeroBuyReq extends BaseReq {

	private MsgReqHeroBuy req;

	public HeroBuyReq(int id) {
		req = new MsgReqHeroBuy().setId(id);
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_HERO_BUY
				.getNumber();
	}
}
