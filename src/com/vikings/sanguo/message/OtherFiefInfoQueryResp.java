package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.OtherFiefInfoClient;
import com.vikings.sanguo.protos.MsgRspOtherFiefInfoQuery;

/**
 * 
 * @author susong
 * 
 */
public class OtherFiefInfoQueryResp extends BaseResp {
	private OtherFiefInfoClient info = null;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspOtherFiefInfoQuery resp = new MsgRspOtherFiefInfoQuery();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		info = OtherFiefInfoClient.convert(resp.getInfo());
	}

	public OtherFiefInfoClient getInfo() {
		return info;
	}
}
