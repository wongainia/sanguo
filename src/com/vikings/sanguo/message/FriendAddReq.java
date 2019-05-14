package com.vikings.sanguo.message;

import java.io.OutputStream;
import java.util.List;

import com.vikings.sanguo.protos.MsgReqFriendAdd;

public class FriendAddReq extends BaseReq {

	private MsgReqFriendAdd req;

	public FriendAddReq(List<Integer> ids) {
		req = new MsgReqFriendAdd().setUseridsList(ids);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_FRIEND_ADD.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
