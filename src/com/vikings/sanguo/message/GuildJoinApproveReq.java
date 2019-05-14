package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqGuildJoinApprove;

public class GuildJoinApproveReq extends BaseReq {

	private MsgReqGuildJoinApprove req;

	public GuildJoinApproveReq(int guildid, int target, int answer) {
		req = new MsgReqGuildJoinApprove().setGuildid(guildid)
				.setTarget(target).setAnswer(answer);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_GUILD_JOIN_APPROVE
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
