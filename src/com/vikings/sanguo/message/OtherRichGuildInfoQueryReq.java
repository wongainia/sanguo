package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqOtherRichGuildInfoQuery;

//更新家族信息
public class OtherRichGuildInfoQueryReq extends BaseReq {

	private MsgReqOtherRichGuildInfoQuery req;

	public OtherRichGuildInfoQueryReq(int guildid) {
		req = new MsgReqOtherRichGuildInfoQuery().setGuildid(guildid);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_OTHER_RICH_GUILD_INFO_QUERY.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
