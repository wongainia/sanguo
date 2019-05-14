package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.protos.MsgRspSelfFiefInfoQuery;
import com.vikings.sanguo.protos.RichFiefInfo;

/**
 * 
 * @author susong
 * 
 */
public class SelfFiefQueryResp extends BaseResp {
	
	private RichFiefInfo fief;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspSelfFiefInfoQuery resp = new MsgRspSelfFiefInfoQuery();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		fief = resp.getInfo();
	}

	public RichFiefInfo getFief() {
		return fief;
	}
}
