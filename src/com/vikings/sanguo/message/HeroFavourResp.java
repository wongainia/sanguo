package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspHeroFavour;

public class HeroFavourResp extends BaseResp {
	private ReturnInfoClient ric;
	private HeroInfoClient hic;

	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspHeroFavour rsp = new MsgRspHeroFavour();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		ric = ReturnInfoClient.convert2Client(rsp.getRi());
		hic = HeroInfoClient.convert(rsp.getHeroInfo());
		Account.heroInfoCache.update(hic);
	}

	public ReturnInfoClient getRic() {
		return ric;
	}

	public HeroInfoClient getHic() {
		return hic;
	}

}
