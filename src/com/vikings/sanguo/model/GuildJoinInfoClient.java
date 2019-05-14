package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.protos.BaseGuildJoinInfo;
import com.vikings.sanguo.protos.ExGuildJoinInfo;
import com.vikings.sanguo.protos.GuildJoinInfo;

// 申请族员信息
public class GuildJoinInfoClient extends UserTimeData {
	private String message; // 留言

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static GuildJoinInfoClient convert(GuildJoinInfo info) {
		GuildJoinInfoClient gic = new GuildJoinInfoClient();
		BaseGuildJoinInfo bi = info.getBi();
		gic.setUserId(bi.getUserid());
		gic.setTime(bi.getTime());
		gic.setMessage(bi.getMessage());
		return gic;
	}

	public static List<GuildJoinInfoClient> convertList(
			List<ExGuildJoinInfo> infos) {
		List<GuildJoinInfoClient> gjics = new ArrayList<GuildJoinInfoClient>();
		if (null == infos)
			return gjics;
		for (ExGuildJoinInfo info : infos) {
			gjics.add(convert(info.getInfo()));
		}
		return gjics;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GuildJoinInfoClient other = (GuildJoinInfoClient) obj;
		if (userId != other.userId)
			return false;
		return true;
	}

	@Override
	public int getType() {
		return TYPE_GUILD_JOIN;
	}

}
