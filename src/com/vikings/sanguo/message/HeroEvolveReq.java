package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqHeroEvolve;

public class HeroEvolveReq extends BaseReq {

	private MsgReqHeroEvolve req;

	public HeroEvolveReq(long heroId, int type) {
		req = new MsgReqHeroEvolve().setHero(heroId).setType(type);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_HERO_EVOLVE
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
