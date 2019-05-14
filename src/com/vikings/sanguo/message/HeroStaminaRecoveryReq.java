package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqHeroStaminaRecovery;

public class HeroStaminaRecoveryReq extends BaseReq {
	private MsgReqHeroStaminaRecovery req;

	public HeroStaminaRecoveryReq(long hero, int currency) {
		req = new MsgReqHeroStaminaRecovery().setCurrency(currency);
		if (hero > 0)
			req.setHero(hero);
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_HERO_STAMINA_RECOVERY.getNumber();
	}

}
