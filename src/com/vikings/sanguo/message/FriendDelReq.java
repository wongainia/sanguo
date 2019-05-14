package com.vikings.sanguo.message;

import java.io.OutputStream;
import java.util.List;

import com.vikings.sanguo.protos.MsgReqFriendDel;

public class FriendDelReq extends BaseReq {

	private MsgReqFriendDel req;

	public FriendDelReq(List<Integer> targetIds) {
		req = new MsgReqFriendDel().setTargetidsList(targetIds);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_FRIEND_DEL
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}
}
