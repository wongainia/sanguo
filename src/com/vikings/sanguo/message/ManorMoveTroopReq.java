package com.vikings.sanguo.message;

import java.io.OutputStream;
import java.util.List;

import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.model.HeroIdInfoClient;
import com.vikings.sanguo.protos.MsgReqManorMoveTroop;
import com.vikings.sanguo.protos.TroopInfo;

public class ManorMoveTroopReq extends BaseReq {

	private MsgReqManorMoveTroop req;

	public ManorMoveTroopReq(long dstFiefid, List<ArmInfoClient> troops,
			int type, List<HeroIdInfoClient> heros) {
		req = new MsgReqManorMoveTroop().setDst(dstFiefid).setType(type);

		if (null != heros && !heros.isEmpty()) {
			req.setHeroInfosList(HeroIdInfoClient.convert2ServerList(heros));
		}

		if (null != troops && !troops.isEmpty()) {
			TroopInfo info = new TroopInfo().setInfosList(ArmInfoClient
					.conver2SerList(troops));
			req.setTroopInfo(info);
		}

	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_MANOR_MOVE_TROOP
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
