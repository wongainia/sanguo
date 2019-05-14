package com.vikings.sanguo.message;

import java.io.OutputStream;
import java.util.List;

import com.vikings.sanguo.model.HeroIdInfoClient;
import com.vikings.sanguo.protos.MsgReqFiefHeroSelect;

public class FiefHeroSelectReq extends BaseReq {
	private MsgReqFiefHeroSelect req;

	public FiefHeroSelectReq(long fiefId, List<HeroIdInfoClient> heros) {
		req = new MsgReqFiefHeroSelect().setFiefid(fiefId);
		if (null != heros && !heros.isEmpty())
			req.setHeroInfosList(HeroIdInfoClient.convert2ServerList(heros));
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_FIEF_HERO_SELECT
				.getNumber();
	}

}
