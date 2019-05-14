package com.vikings.sanguo.message;

import java.io.OutputStream;
import java.util.List;

import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.protos.MsgReqDungeonDivine;
import com.vikings.sanguo.protos.TroopInfo;

public class BattleDungeonDivineReq extends BaseReq {

	private MsgReqDungeonDivine req;

	public BattleDungeonDivineReq(int actId, int campaignId, int mode,
			long hero, List<ArmInfoClient> troops) {
		req = new MsgReqDungeonDivine();
		req.setActid(actId).setCampaignid(campaignId).setMode(mode);
		if (hero > 0)
			req.setHero(hero);
		if (null != troops && !troops.isEmpty()) {
			TroopInfo info = new TroopInfo().setInfosList(ArmInfoClient
					.conver2SerList(troops));
			req.setTroop(info);
		}
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_DUNGEON_DIVINE
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
