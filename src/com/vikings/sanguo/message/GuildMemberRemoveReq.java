package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqGuildMemberRemove;

public class GuildMemberRemoveReq extends BaseReq {

	private MsgReqGuildMemberRemove req;

	public GuildMemberRemoveReq(int guildid, int target) {
		req = new MsgReqGuildMemberRemove().setGuildid(guildid).setTarget(
				target);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_GUILD_MEMBER_REMOVE.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
