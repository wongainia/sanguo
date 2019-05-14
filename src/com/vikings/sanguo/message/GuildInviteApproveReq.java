package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqGuildInviteApprove;

public class GuildInviteApproveReq extends BaseReq {

	private MsgReqGuildInviteApprove req;

	public GuildInviteApproveReq(int guildid, int answer) {
		req = new MsgReqGuildInviteApprove().setGuildid(guildid)
				.setAnswer(answer);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_GUILD_INVITE_APPROVE.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
