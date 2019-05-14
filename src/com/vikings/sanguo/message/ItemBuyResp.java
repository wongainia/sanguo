package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspItemBuy;

/**
 * 
 * @author Brad.Chen
 * 
 */
public class ItemBuyResp extends BaseResp {

	private ReturnInfoClient ri;

	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspItemBuy rsp = new MsgRspItemBuy();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		ri = ReturnInfoClient.convert2Client(rsp.getRi());
	}

	public ReturnInfoClient getRi() {
		return ri;
	}

}
