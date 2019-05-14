package com.vikings.sanguo.message;

import java.util.ArrayList;
import java.util.List;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.protos.MsgRspLordFiefIdQuery;

/**
 * 
 * @author susong
 * 
 */
public class LordFiefIdQueryResp extends BaseResp {
	private List<Long> fiefIds = new ArrayList<Long>();

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspLordFiefIdQuery resp = new MsgRspLordFiefIdQuery();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		fiefIds.addAll(resp.getFiefidsList());
	}

	public List<Long> getFiefIds() {
		return fiefIds;
	}
}
