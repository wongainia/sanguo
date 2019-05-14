package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspHeroAbandon;

/**
 * 
 * @author susong
 * 
 */
public class HeroAbandonResp extends BaseResp {
	private ReturnInfoClient ri;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspHeroAbandon resp = new MsgRspHeroAbandon();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		ri = ReturnInfoClient.convert2Client(resp.getInfo());
	}

	public ReturnInfoClient getRi() {
		return ri;
	}
}
