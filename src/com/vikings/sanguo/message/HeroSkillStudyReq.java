package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqHeroSkillStudy;

public class HeroSkillStudyReq extends BaseReq {
	private MsgReqHeroSkillStudy req;

	public HeroSkillStudyReq(long id, int slotId, int skillId) {
		req = new MsgReqHeroSkillStudy().setHero(id).setSlotid(slotId)
				.setSkillid(skillId);
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_HERO_SKILL_STUDY.getNumber();
	}

}
