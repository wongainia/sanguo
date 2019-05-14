package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspHeroEnhance;

public class HeroEnhanceResp extends BaseResp {
	private ReturnInfoClient ri;
	private HeroInfoClient info;

	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspHeroEnhance resp = new MsgRspHeroEnhance();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		ri = ReturnInfoClient.convert2Client(resp.getRi());
		info = HeroInfoClient.convert(resp.getInfo());
		if (null != info)
			Account.heroInfoCache.update(info);
	}

	public ReturnInfoClient getRi() {
		return ri;
	}

	public HeroInfoClient getInfo() {
		return info;
	}

}
