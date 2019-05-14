package com.vikings.sanguo.message;

import java.util.ArrayList;
import java.util.List;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.protos.MsgRspRecommendUser;

/**
 * 
 * @author susong
 * 
 */
public class RecommendUserResp extends BaseResp {
	private List<Integer> userIds = new ArrayList<Integer>();

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspRecommendUser rsp = new MsgRspRecommendUser();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		userIds.addAll(rsp.getUseridsList());
	}

	public List<Integer> getUserIds() {
		return userIds;
	}
}
