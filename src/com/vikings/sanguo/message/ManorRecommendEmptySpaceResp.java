package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.protos.MsgRspFiefRecommendEmptySpace;

/**
 * 
 * @author susong
 * 
 */
public class ManorRecommendEmptySpaceResp extends BaseResp {
	private long pos;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspFiefRecommendEmptySpace resp = new MsgRspFiefRecommendEmptySpace();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		pos = resp.getZoneid();
	}

	public long getPos() {
		return pos;
	}

}
