package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqGuildJoinAsk;

public class GuildJoinAskReq extends BaseReq {

	private MsgReqGuildJoinAsk req;

	public GuildJoinAskReq(int guildid, String message) {
		req = new MsgReqGuildJoinAsk().setGuildid(guildid).setMessage(message);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_GUILD_JOIN_ASK
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
