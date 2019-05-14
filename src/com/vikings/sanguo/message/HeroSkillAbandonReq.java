package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqHeroSkillAbandon;

public class HeroSkillAbandonReq extends BaseReq {
	private MsgReqHeroSkillAbandon req;

	public HeroSkillAbandonReq(long id, int slotId) {
		req = new MsgReqHeroSkillAbandon().setHero(id).setSlotid(slotId);
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_HERO_SKILL_ABANDON.getNumber();
	}

}
