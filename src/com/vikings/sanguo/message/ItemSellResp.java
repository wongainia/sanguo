package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspItemSell;

public class ItemSellResp extends BaseResp {

	private ReturnInfoClient ri;

	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspItemSell rsp = new MsgRspItemSell();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		ri = ReturnInfoClient.convert2Client(rsp.getRi());
	}

	public ReturnInfoClient getRi() {
		return ri;
	}

}
