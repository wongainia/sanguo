package com.vikings.sanguo.message;

import java.io.OutputStream;
import java.util.List;

import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.model.HeroIdInfoClient;
import com.vikings.sanguo.protos.MsgReqArenaConf;
import com.vikings.sanguo.protos.TroopInfo;

public class BloodConfReq extends BaseReq {
	private MsgReqArenaConf req;

	public BloodConfReq(List<HeroIdInfoClient> heros, List<ArmInfoClient> troops) {
		req = new MsgReqArenaConf();
		if (null != heros && !heros.isEmpty()) {
			req.setHerosList(HeroIdInfoClient.convert2ServerList(heros));
		}
		if (null != troops && !troops.isEmpty()) {
			TroopInfo info = new TroopInfo().setInfosList(ArmInfoClient
					.conver2SerList(troops));
			req.setTroop(info);
		}
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_BLOOD_CONF
				.getNumber();
	}
}
