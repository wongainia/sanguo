package com.vikings.sanguo.message;

import java.util.ArrayList;
import java.util.List;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.protos.MsgRspFriendAdd;

/**
 * 
 * @author susong
 * 
 */
public class FriendAddResp extends BaseResp {
	private List<Integer> failUserIds = new ArrayList<Integer>();

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspFriendAdd resp = new MsgRspFriendAdd();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		failUserIds.addAll(resp.getFailedUseridsList());
	}

	public List<Integer> getFailUserIds() {
		return failUserIds;
	}

}
