package com.vikings.sanguo.model;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.GuildLogEvent;
import com.vikings.sanguo.protos.GuildLogInfo;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.StringUtil;

public class GuildLogInfoClient {
	private long id; // 唯一id
	private int guildid; // 家族id
	private int userid; // 玩家id
	private int targetId; // 目标id
	private GuildLogEvent event; // 事件
	private BriefUserInfoClient user;
	private BriefUserInfoClient target;
	private int time;

	public BriefUserInfoClient getUser() {
		return user;
	}

	public void setUser(BriefUserInfoClient user) {
		this.user = user;
	}

	private String desc;

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getGuildid() {
		return guildid;
	}

	public void setGuildid(int guildid) {
		this.guildid = guildid;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public GuildLogEvent getEvent() {
		return event;
	}

	public void setEvent(GuildLogEvent event) {
		this.event = event;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public void setTarget(BriefUserInfoClient target) {
		this.target = target;
	}

	public void setTargetId(int targetId) {
		this.targetId = targetId;
	}

	public BriefUserInfoClient getTarget() {
		return target;
	}

	public int getTargetId() {
		return targetId;
	}

	public static GuildLogInfoClient convert(GuildLogInfo info)
			throws GameException {
		GuildLogInfoClient glic = new GuildLogInfoClient();
		glic.setId(info.getId());
		glic.setGuildid(info.getGuildid());
		glic.setUserid(info.getUserid());
		glic.setTargetId(info.getTargetid());
		glic.setEvent(info.getEvent());
		glic.setTime(info.getTime());
		return glic;
	}

	// GUILD_LOG_EVENT_JOIN(1) ： userId 加入了 ××× 家族
	// GUILD_LOG_EVENT_QUIT(2)： userId 退出了 ××× 家族
	// GUILD_LOG_EVENT_REMOVE(4) ： targetId 被踢出了××× 家族
	// GUILD_LOG_EVENT_INVITE_REFUSE(5), userId 拒绝加入××× 家族
	// GUILD_LOG_EVENT_ASSIGN(6); 原族长将族长之位转让给了 targetid

	public String toDesc() {
		StringBuilder builder = new StringBuilder();
		builder.append(DateUtil.formatBefore(time)).append("前，");
		if (event == GuildLogEvent.GUILD_LOG_EVENT_INVITE_REFUSE) {
			builder.append(
					StringUtil.color(user.getHtmlNickName(), R.color.k7_color10)).append(
					"拒绝加入家族");
		} else if (event == GuildLogEvent.GUILD_LOG_EVENT_JOIN) {
			builder.append(
					StringUtil.color(user.getHtmlNickName(), R.color.k7_color10)).append(
					"加入家族");
		} else if (event == GuildLogEvent.GUILD_LOG_EVENT_QUIT) {
			builder.append(
					StringUtil.color(user.getHtmlNickName(), R.color.k7_color10)).append(
					"退出了家族");
		} else if (event == GuildLogEvent.GUILD_LOG_EVENT_REMOVE) {
			builder.append(
					StringUtil.color(
							target.getHtmlNickName(), R.color.k7_color10)).append("被踢出了家族");

		} else if (event == GuildLogEvent.GUILD_LOG_EVENT_ASSIGN) {
			builder.append("原族长将族长之位转让给了").append(
					StringUtil.color(target.getHtmlNickName(), R.color.k7_color10));
		} else if (event == GuildLogEvent.GUILD_LOG_EVENT_JOIN_ASK) {
			builder.append(
					StringUtil.color(user.getHtmlNickName(), R.color.k7_color10)).append(
					"申请加入家族");
		}
		return builder.toString();
	}
}
