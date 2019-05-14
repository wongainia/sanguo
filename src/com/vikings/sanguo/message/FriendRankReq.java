package com.vikings.sanguo.message;

import java.io.OutputStream;
import java.util.List;

import com.vikings.sanguo.protos.MsgReqUserStatData;

public class FriendRankReq extends BaseReq {

	private MsgReqUserStatData req;

	public FriendRankReq(List<Integer> friendsIdList) {
		req = new MsgReqUserStatData().setUseridsList(friendsIdList);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_USER_STAT_DATA2
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
