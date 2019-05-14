package com.vikings.sanguo.message;

import java.io.OutputStream;
import java.util.List;

import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.model.HeroIdInfoClient;
import com.vikings.sanguo.protos.MsgReqFiefMoveTroop;
import com.vikings.sanguo.protos.TroopInfo;

public class FiefMoveTroopReq extends BaseReq {

	private MsgReqFiefMoveTroop req;

	public FiefMoveTroopReq(long src, int type, List<ArmInfoClient> troops,
			List<HeroIdInfoClient> heros) {
		req = new MsgReqFiefMoveTroop().setSrc(src).setType(type);
		if (null != troops && !troops.isEmpty()) {
			TroopInfo info = new TroopInfo().setInfosList(ArmInfoClient
					.conver2SerList(troops));
			req.setTroopInfo(info);
		}
		if (null != heros && !heros.isEmpty()) {
			req.setHeroInfosList(HeroIdInfoClient.convert2ServerList(heros));
		}
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_FIEF_MOVE_TROOP
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
