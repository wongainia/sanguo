package com.vikings.sanguo.model;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.protos.PlayerWantedInfo;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.StringUtil;

public class PlayerWantedInfoClient {
	private PlayerWantedInfo info;
	private BriefUserInfoClient briefUser; // 发布user
	private BriefUserInfoClient targetUser;// 被追杀user

	public PlayerWantedInfoClient(PlayerWantedInfo info) {
		this.info = info;
	}

	public PlayerWantedInfo getInfo() {
		return info;
	}

	public void setInfo(PlayerWantedInfo info) {
		this.info = info;
	}

	public BriefUserInfoClient getBriefUser() {
		return briefUser;
	}

	public void setBriefUser(BriefUserInfoClient briefUser) {
		this.briefUser = briefUser;
	}

	public BriefUserInfoClient getTargetUser() {
		return targetUser;
	}

	public void setTargetUser(BriefUserInfoClient targetUser) {
		this.targetUser = targetUser;
	}

	/**
	 * 我被追杀的描述
	 * 
	 * @return
	 */
	public String getWantedMeDesc() {
		return StringUtil.color(
				briefUser.getNickName() + "(ID:" + briefUser.getId()
						+ ")对你使用了江湖追杀令！", R.color.k7_color8)
				+ "<br/><br/>你将被"
				+ StringUtil.color(briefUser.getCountryName(),
						R.color.k7_color8)
				+ "的所有玩家通缉"
				+ DateUtil.formatTime(CacheMgr.dictCache.getDictInt(
						Dict.TYPE_WANTED, 1))
				+ "，江湖上即将刮起腥风血雨，请立刻寻找支援，或者在商店购买追杀令对他进行反击！";
	}
}
