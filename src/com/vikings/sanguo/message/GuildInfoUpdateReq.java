package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqGuildInfoUpdate;

//更新家族信息
public class GuildInfoUpdateReq extends BaseReq {

	private MsgReqGuildInfoUpdate req;

	public GuildInfoUpdateReq(int guildid, String desc, int image,
			String announcement, boolean autoJoin) {
		req = new MsgReqGuildInfoUpdate().setGuildid(guildid).setDesc(desc)
				.setImage(image).setAnnouncement(announcement)
				.setAutoJoin(autoJoin);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_GUILD_INFO_UPDATE
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
