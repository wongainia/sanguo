package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqGuildMemberQuit;

public class GuildMemberQuitReq extends BaseReq {

	private MsgReqGuildMemberQuit req;

	public GuildMemberQuitReq(int guildid) {
		req = new MsgReqGuildMemberQuit().setGuildid(guildid);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_GUILD_MEMBER_QUIT.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
