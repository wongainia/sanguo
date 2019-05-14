package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqDungeonReward;

public class DungeonRewardReq extends BaseReq {

	private MsgReqDungeonReward req;

	public DungeonRewardReq(int actId) {
		req = new MsgReqDungeonReward();
		req.setActid(actId);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_DUNGEON_REWARD
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
