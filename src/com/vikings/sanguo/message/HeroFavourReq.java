package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqHeroFavour;

public class HeroFavourReq extends BaseReq {
	private MsgReqHeroFavour req;

	public HeroFavourReq(long hero, int slotId) {
		req = new MsgReqHeroFavour().setHero(hero).setSlotid(slotId);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_HERO_FAVOUR
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
