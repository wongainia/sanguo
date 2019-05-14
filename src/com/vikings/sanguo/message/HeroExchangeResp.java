package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspHeroExchange;

public class HeroExchangeResp extends BaseResp {
	private ReturnInfoClient ri;
	private HeroInfoClient hic;

	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspHeroExchange resp = new MsgRspHeroExchange();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		ri = ReturnInfoClient.convert2Client(resp.getRi());
		hic = HeroInfoClient.convert(resp.getInfo());
		Account.heroInfoCache.update(hic);
	}

	public ReturnInfoClient getRi() {
		return ri;
	}

	public HeroInfoClient getHic() {
		return hic;
	}
}
