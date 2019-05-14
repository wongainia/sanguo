package com.vikings.sanguo.message;

import java.io.OutputStream;
import java.util.List;

import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.model.HeroIdInfoClient;
import com.vikings.sanguo.protos.MsgReqBattleReinfor;
import com.vikings.sanguo.protos.TroopInfo;

public class BattleReinforReq extends BaseReq {

	private MsgReqBattleReinfor req;

	public BattleReinforReq(int targetId, long dst, int holyCost, int type,
			int targetType, List<ArmInfoClient> troops,
			List<HeroIdInfoClient> heros) {
		req = new MsgReqBattleReinfor();
		req.setDstFiefid(dst).setType(type).setTarget(targetId)
				.setTargetType(targetType);

		if (null != heros && !heros.isEmpty())
			req.setHeroInfosList(HeroIdInfoClient.convert2ServerList(heros));
		if (holyCost > 0)
			req.setHolyCost(holyCost);
		if (null != troops && !troops.isEmpty()) {
			TroopInfo info = new TroopInfo().setInfosList(ArmInfoClient
					.conver2SerList(troops));
			req.setInfo(info);
		}

	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_BATTLE_REINFOR
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
