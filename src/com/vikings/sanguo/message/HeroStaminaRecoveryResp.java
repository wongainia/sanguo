package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspHeroStaminaRecovery;

public class HeroStaminaRecoveryResp extends BaseResp {
	private HeroInfoClient info;
	private ReturnInfoClient ri;

	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspHeroStaminaRecovery resp = new MsgRspHeroStaminaRecovery();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		info = HeroInfoClient.convert(resp.getInfo());
		Account.heroInfoCache.update(info);
		ri = ReturnInfoClient.convert2Client(resp.getReturnInfo());
	}

	public HeroInfoClient getInfo() {
		return info;
	}

	public ReturnInfoClient getRi() {
		return ri;
	}

}
