package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspHeroEvolve;

/**
 * 
 * @author susong
 * 
 */
public class HeroEvolveResp extends BaseResp {

	private ReturnInfoClient ri;

	private HeroInfoClient hero;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspHeroEvolve resp = new MsgRspHeroEvolve();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		ri = ReturnInfoClient.convert2Client(resp.getRi());
		hero = HeroInfoClient.convert(resp.getHeroInfo());
		if (null != hero)
			Account.heroInfoCache.update(hero);
	}

	public ReturnInfoClient getRi() {
		return ri;
	}

	public HeroInfoClient getHero() {
		return hero;
	}

}
