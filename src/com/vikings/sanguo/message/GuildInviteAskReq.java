package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqGuildInviteAsk;

public class GuildInviteAskReq extends BaseReq {

	private MsgReqGuildInviteAsk req;

	public GuildInviteAskReq(int guildid, int target, String message) {
		req = new MsgReqGuildInviteAsk().setGuildid(guildid).setTarget(target)
				.setMessage(message);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_GUILD_INVITE_ASK.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
