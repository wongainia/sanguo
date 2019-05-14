package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqRichGuildVersionQuery;

public class RichGuildVersionQueryReq extends BaseReq {

	private MsgReqRichGuildVersionQuery req;

	public RichGuildVersionQueryReq(int guildid) {
		req = new MsgReqRichGuildVersionQuery().setGuildid(guildid);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_RICH_GUILD_VERSION_QUERY.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
