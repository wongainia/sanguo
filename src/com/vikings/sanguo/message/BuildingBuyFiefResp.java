package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.LordFiefInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspFiefBuildingBuy;

/**
 * 
 * @author susong
 * 
 */
public class BuildingBuyFiefResp extends BaseResp {

	private ReturnInfoClient ri;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspFiefBuildingBuy resp = new MsgRspFiefBuildingBuy();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		ri = ReturnInfoClient.convert2Client(resp.getRi());
		LordFiefInfoClient lord = LordFiefInfoClient.convert(resp
				.getLordFiefInfo());
		Account.richFiefCache.update(lord, null);
	}

	public ReturnInfoClient getRi() {
		return ri;
	}

}
