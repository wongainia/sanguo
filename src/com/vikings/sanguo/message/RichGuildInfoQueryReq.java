package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqRichGuildInfoQuery;

//更新家族信息
public class RichGuildInfoQueryReq extends BaseReq {

	private MsgReqRichGuildInfoQuery req;

	public RichGuildInfoQueryReq(int guildid) {
		req = new MsgReqRichGuildInfoQuery().setGuildid(guildid);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_RICH_GUILD_INFO_QUERY.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
