package com.vikings.sanguo.message;

import java.io.OutputStream;
import java.util.List;

import com.vikings.sanguo.model.HeroIdInfoClient;
import com.vikings.sanguo.protos.MsgReqDungeonAttack;

public class DungeonAttackReq extends BaseReq {

	private MsgReqDungeonAttack req;

	public DungeonAttackReq(int actId, int campaignId,
			List<HeroIdInfoClient> heros, boolean help) {
		req = new MsgReqDungeonAttack();
		req.setActid(actId).setCampaignid(campaignId).setHelp(help ? 1 : 0);
		if (null != heros && !heros.isEmpty())
			req.setHeroInfosList(HeroIdInfoClient.convert2ServerList(heros));
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_DUNGEON_ATTACK
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
