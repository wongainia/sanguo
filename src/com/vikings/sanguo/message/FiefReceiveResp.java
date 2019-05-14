package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.LordFiefInfoClient;
import com.vikings.sanguo.model.ManorInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspFiefReceive;

/**
 * 
 * @author susong
 * 
 */
public class FiefReceiveResp extends BaseResp {
	private ReturnInfoClient ri;
	private ManorInfoClient mic;

	private LordFiefInfoClient lord;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspFiefReceive resp = new MsgRspFiefReceive();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		ri = ReturnInfoClient.convert2Client(resp.getRi());
		mic = ManorInfoClient.convert(resp.getMi());
		lord = LordFiefInfoClient.convert(resp.getFi());
		Account.manorInfoClient.update(mic);
		Account.richFiefCache.update(lord, null);
	}

	public ReturnInfoClient getRi() {
		return ri;
	}

	public ManorInfoClient getMic() {
		return mic;
	}

}
