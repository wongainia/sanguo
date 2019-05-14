package com.vikings.sanguo.model;

public class GuildInvitedUserData {
	private BriefUserInfoClient user;
	private GuildInviteInfoClient guildInviteInfoClient;

	public BriefUserInfoClient getUser() {
		return user;
	}

	public void setUser(BriefUserInfoClient user) {
		this.user = user;
	}

	public GuildInviteInfoClient getGuildInviteInfoClient() {
		return guildInviteInfoClient;
	}

	public void setGuildInviteInfoClient(
			GuildInviteInfoClient guildInviteInfoClient) {
		this.guildInviteInfoClient = guildInviteInfoClient;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof GuildInvitedUserData) {
			return getUser().equals(((GuildInvitedUserData) o).getUser());
		} else
			return false;
	}

}