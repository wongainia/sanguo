package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqGuildLeaderAssign;

public class GuildLeaderAssignReq extends BaseReq {

	private MsgReqGuildLeaderAssign req;

	public GuildLeaderAssignReq(int guildid, int target) {
		req = new MsgReqGuildLeaderAssign().setGuildid(guildid).setTarget(
				target);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_GUILD_LEADER_ASSIGN.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
