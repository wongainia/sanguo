package com.vikings.sanguo.message;

import java.io.OutputStream;
import java.util.List;

import com.vikings.sanguo.protos.MsgReqUserBattleInfoQuery;

public class UserBattleInfoQueryReq extends BaseReq {

	private MsgReqUserBattleInfoQuery req;

	public UserBattleInfoQueryReq(List<Integer> userIds, int flag) {
		req = new MsgReqUserBattleInfoQuery().setUseridsList(userIds).setFlag(
				flag);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_USER_BATTLE_INFO_QUERY
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
