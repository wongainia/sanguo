package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.protos.BaseGuildInviteInfo;
import com.vikings.sanguo.protos.ExGuildInviteInfo;
import com.vikings.sanguo.protos.GuildInviteInfo;

public class GuildInviteInfoClient extends UserTimeData {

	@Override
	public boolean equals(Object o) {
		if (o instanceof GuildInviteInfoClient) {
			return getUserId() == ((GuildInviteInfoClient) o).getUserId();
		} else {
			return false;
		}
	}

	public static GuildInviteInfoClient convert(GuildInviteInfo info) {
		GuildInviteInfoClient giic = new GuildInviteInfoClient();
		BaseGuildInviteInfo bi = info.getBi();
		giic.setUserId(bi.getUserid());
		giic.setTime(bi.getTime());
		return giic;
	}

	public static List<GuildInviteInfoClient> convertList(
			List<ExGuildInviteInfo> infos) {
		List<GuildInviteInfoClient> giics = new ArrayList<GuildInviteInfoClient>();
		if (infos == null)
			return giics;
		for (ExGuildInviteInfo info : infos) {
			giics.add(convert(info.getInfo()));
		}
		return giics;
	}

	@Override
	public int getType() {
		return TYPE_GUILD_INVITE;
	}

	public void clear() {
		this.time = 0;
		this.userId = 0;
		this.briefUser = null;
		this.approve = false;
	}

	public GuildInviteInfoClient copy() {
		GuildInviteInfoClient giic = new GuildInviteInfoClient();
		giic.setTime(this.time);
		giic.setUserId(this.userId);
		giic.setBriefUser(this.briefUser);
		giic.setApprove(this.approve);
		return giic;
	}

}
