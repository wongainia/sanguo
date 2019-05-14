package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqGuildLevelUp;

public class GuildLevelUpReq extends BaseReq {

	private MsgReqGuildLevelUp req;

	public GuildLevelUpReq(int guildId) {
		req = new MsgReqGuildLevelUp().setGuildid(guildId);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_GUILD_LEVELUP
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
