package com.vikings.sanguo.message;

import java.util.ArrayList;
import java.util.List;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.protos.MsgRspFavoriteFiefQuery;

/**
 * 
 * @author susong
 * 
 */
public class FavoriteFiefQueryResp extends BaseResp {
	List<Long> fiefids = new ArrayList<Long>();

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspFavoriteFiefQuery rsp = new MsgRspFavoriteFiefQuery();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		fiefids = rsp.getFiefidsList();
	}

	public List<Long> getFiefids() {
		return fiefids;
	}

}
