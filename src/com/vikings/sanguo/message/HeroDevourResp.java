package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspHeroDevour;

/**
 * 
 * @author susong
 * 
 */
public class HeroDevourResp extends BaseResp {

	private ReturnInfoClient ri;

	private HeroInfoClient hero;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspHeroDevour resp = new MsgRspHeroDevour();
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
