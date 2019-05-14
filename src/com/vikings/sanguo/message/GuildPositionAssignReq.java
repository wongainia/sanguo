package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqGuildPositionAssign;

public class GuildPositionAssignReq extends BaseReq {

	private MsgReqGuildPositionAssign req;

	public GuildPositionAssignReq(int guildId, int target, int position) {
		req = new MsgReqGuildPositionAssign().setGuildid(guildId)
				.setTarget(target).setPosition(position);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_GUILD_POSITION_ASSIGN
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
