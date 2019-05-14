package com.vikings.sanguo.model;

import com.vikings.sanguo.cache.Account;

//家族群聊消息结构
public class GuildChatData extends ChatData {

	private int type;
	private int guildid;

	private boolean fake = false;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getGuildid() {
		return guildid;
	}

	public void setGuildid(int guildid) {
		this.guildid = guildid;
	}

	public void setFake(boolean fake) {
		this.fake = fake;
	}

	public boolean isFake() {
		return fake;
	}

	public boolean isWorldChatData() {
		return guildid == 0;
	}

	public boolean isCountryChatData() {
		return guildid == Account.user.getCountry();
	}

	public boolean isGuildChatData() {
		return guildid > 99999;
	}
}
