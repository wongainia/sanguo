package com.vikings.sanguo.message;

import java.io.OutputStream;
import java.util.List;

import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.model.HeroIdInfoClient;
import com.vikings.sanguo.protos.MsgReqBattleRiseTroop;
import com.vikings.sanguo.protos.TroopInfo;

public class BattleRiseTroopReq extends BaseReq {

	private MsgReqBattleRiseTroop req;

	public BattleRiseTroopReq(long dstFiefId, int type, int targetUserId,
			int battleType, List<ArmInfoClient> troops,
			List<HeroIdInfoClient> heros, int cost) {
		req = new MsgReqBattleRiseTroop();
		req.setDstFiefid(dstFiefId).setType(type).setTarget(targetUserId)
				.setBattleType(battleType).setCost(cost);
		if (null != heros && !heros.isEmpty())
			req.setHeroInfosList(HeroIdInfoClient.convert2ServerList(heros));
		if (null != troops && !troops.isEmpty()) {
			TroopInfo info = new TroopInfo().setInfosList(ArmInfoClient
					.conver2SerList(troops));
			req.setInfo(info);
		}

	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_BATTLE_RISE_TROOP
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
