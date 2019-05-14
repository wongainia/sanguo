package com.vikings.sanguo.message;

import java.io.OutputStream;
import java.util.List;

import com.vikings.sanguo.protos.MsgReqHeroDevour;

public class HeroDevourReq extends BaseReq {

	private MsgReqHeroDevour req;

	public HeroDevourReq(long heroId, List<Long> bagIds, int type) {
		req = new MsgReqHeroDevour().setHero(heroId).setType(type);
		if (null != bagIds)
			req.setBagidsList(bagIds);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_HERO_DEVOUR
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
