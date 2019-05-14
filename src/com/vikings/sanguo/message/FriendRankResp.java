package com.vikings.sanguo.message;

import java.util.ArrayList;
import java.util.List;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.UserStatDataClient;
import com.vikings.sanguo.protos.MsgRspUserStatData;
import com.vikings.sanguo.protos.UserStatData;

public class FriendRankResp extends BaseResp {
	private List<UserStatDataClient> friendsRankList;

	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspUserStatData resp = new MsgRspUserStatData();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		friendsRankList = new ArrayList<UserStatDataClient>();
		for (UserStatData data : resp.getDatasList()) {
			UserStatDataClient rankInfo = UserStatDataClient.convert2Client(data);
			friendsRankList.add(rankInfo);
		}
	}

	public List<UserStatDataClient> getRiList() {
		return friendsRankList;
	}

}
