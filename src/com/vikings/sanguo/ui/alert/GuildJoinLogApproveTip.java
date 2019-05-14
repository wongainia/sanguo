package com.vikings.sanguo.ui.alert;

import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.GuildJoinInfoClient;
import com.vikings.sanguo.model.LogInfoClient;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.StringUtil;

public class GuildJoinLogApproveTip extends GuildJoinApproveTip {

	private LogInfoClient log;
	private GuildJoinInfoClient gjic;
	private int guildId;

	public GuildJoinLogApproveTip(LogInfoClient log) {
		super();
		this.log = log;
		this.guildId = log.getGuildInfo().getId();
	}

	public GuildJoinLogApproveTip(GuildJoinInfoClient gjic, int guildId) {
		super();
		this.gjic = gjic;
		this.guildId = guildId;
	}

	@Override
	protected String getDesc() {
		if (null != log) {
			return log.getJoinDesc();
		} else if (null != gjic) {
			StringBuilder buf = new StringBuilder();
			buf.append(DateUtil.formatBefore(gjic.getTime()))
					.append("前，")
					.append((null != gjic.getBriefUser() ? StringUtil
							.nickNameColor(gjic.getBriefUser()) : ""))
					.append("申请加入家族");
			return buf.toString();
		} else {
			return "";
		}
	}

	@Override
	protected BriefUserInfoClient getBriefUser() {
		if (null != log) {
			return log.getFromUser();
		} else if (null != gjic) {
			return gjic.getBriefUser();
		} else {
			return null;
		}
	}

	@Override
	protected int getGuildId() {
		return guildId;
	}

}
