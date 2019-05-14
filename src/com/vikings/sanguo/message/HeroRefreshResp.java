package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.HeroShopInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspHeroRefresh;

public class HeroRefreshResp extends BaseResp {
	private ReturnInfoClient ri;
	private HeroShopInfoClient hsic;
	private HeroInfoClient hic;

	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspHeroRefresh resp = new MsgRspHeroRefresh();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		if (resp.hasHeroInfo()) {
			hic = HeroInfoClient.convert(resp.getHeroInfo());
			Account.heroInfoCache.update(hic);
		}
		ri = ReturnInfoClient.convert2Client(resp.getRi());
		if (resp.hasShopInfo()) {
			hsic = HeroShopInfoClient.convert(resp.getShopInfo());
			Account.myLordInfo.updateHeroShopInfoClient(hsic);
		}
	}

	public ReturnInfoClient getRi() {
		return ri;
	}

	public HeroShopInfoClient getHsic() {
		return hsic;
	}

	public HeroInfoClient getHic() {
		return hic;
	}
}
