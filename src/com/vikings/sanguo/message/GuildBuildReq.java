package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqGuildBuild;

//创建家族
public class GuildBuildReq extends BaseReq {

	private MsgReqGuildBuild req;

	public GuildBuildReq(String name, int type) {
		req = new MsgReqGuildBuild().setName(name).setType(type);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_GUILD_BUILD.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
