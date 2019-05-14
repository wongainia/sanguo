package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.FiefInfoClient;
import com.vikings.sanguo.model.LordFiefInfoClient;
import com.vikings.sanguo.model.ManorInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspFiefMoveTroop;

/**
 * 
 * @author susong
 * 
 */
public class FiefMoveTroopResp extends BaseResp {
	private ReturnInfoClient ri;
	private FiefInfoClient fiefInfo;
	private LordFiefInfoClient lordFiefInfo;
	private ManorInfoClient manorInfo;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspFiefMoveTroop resp = new MsgRspFiefMoveTroop();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		ri = ReturnInfoClient.convert2Client(resp.getRi());
		fiefInfo = FiefInfoClient.convert(resp.getFiefInfo());
		lordFiefInfo = LordFiefInfoClient.convert(resp.getLordFiefInfo());
		manorInfo = ManorInfoClient.convert(resp.getManorInfo());
		Account.richFiefCache.update(lordFiefInfo, fiefInfo);

		fiefInfo = FiefInfoClient.convert(resp.getDstFiefInfo());
		Account.richFiefCache.update(null, fiefInfo);

		Account.manorInfoClient.update(manorInfo);
	}

	public ReturnInfoClient getRi() {
		return ri;
	}

}
