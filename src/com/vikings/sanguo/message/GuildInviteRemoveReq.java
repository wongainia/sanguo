package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqGuildInviteRemove;

public class GuildInviteRemoveReq extends BaseReq {

	private MsgReqGuildInviteRemove req;

	public GuildInviteRemoveReq(int guildid, int target) {
		req = new MsgReqGuildInviteRemove().setGuildid(guildid)
				.setTarget(target);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_GUILD_INVITE_REMOVE.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
