package com.vikings.sanguo.model;

public class GuildBattleData {
	private BriefBattleInfoClient briefBattleInfoClient;
	private BriefGuildInfoClient briefGuildInfoClient;

	public BriefGuildInfoClient getBriefGuildInfoClient() {
		return briefGuildInfoClient;
	}

	public void setBriefGuildInfoClient(
			BriefGuildInfoClient briefGuildInfoClient) {
		this.briefGuildInfoClient = briefGuildInfoClient;
	}

	public BriefBattleInfoClient getBriefBattleInfoClient() {
		return briefBattleInfoClient;
	}

	public void setBriefBattleInfoClient(
			BriefBattleInfoClient briefBattleInfoClient) {
		this.briefBattleInfoClient = briefBattleInfoClient;
	}
}