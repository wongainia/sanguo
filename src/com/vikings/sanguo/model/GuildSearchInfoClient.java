package com.vikings.sanguo.model;

import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.GuildSearchInfo;

public class GuildSearchInfoClient {
	private GuildSearchInfo info;
	private GuildProp guildProp;
	private BriefUserInfoClient briefUser;

	public BriefUserInfoClient getBriefUser() {
		return briefUser;
	}

	public void setBriefUser(BriefUserInfoClient briefUser) {
		this.briefUser = briefUser;
	}

	public GuildSearchInfoClient(GuildSearchInfo info,
			BriefUserInfoClient briefUser) {
		this(info);
		this.briefUser = briefUser;
	}

	public GuildSearchInfoClient(GuildSearchInfo info) {
		this.info = info;
		if (null != info) {
			try {
				guildProp = (GuildProp) CacheMgr.guildPropCache.get(info
						.getLevel());
			} catch (GameException e) {

			}
		}
	}

	public GuildSearchInfo getInfo() {
		return info;
	}

	public void setInfo(GuildSearchInfo info) {
		this.info = info;
	}

	public GuildProp getGuildProp() {
		return guildProp;
	}

	public void setGuildProp(GuildProp guildProp) {
		this.guildProp = guildProp;
	}

	public BriefGuildInfoClient brief() {
		BriefGuildInfoClient bgic = new BriefGuildInfoClient();
		if (null != info) {
			bgic.setId(info.getId());
			bgic.setImage(info.getImage());
			bgic.setName(info.getName());
			bgic.setLevel(info.getLevel());
		}
		return bgic;
	}

}
