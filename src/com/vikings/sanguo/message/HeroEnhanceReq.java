package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqHeroEnhance;

public class HeroEnhanceReq extends BaseReq {
	private MsgReqHeroEnhance req;

	public HeroEnhanceReq(long id, int type) {
		req = new MsgReqHeroEnhance().setHero(id).setType(type);
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_HERO_ENHANCE
				.getNumber();
	}

}
