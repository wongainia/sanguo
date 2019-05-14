/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-10-11 下午12:07:57
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.protos.UserRankInfo;
import com.vikings.sanguo.utils.BytesUtil;
import com.vikings.sanguo.utils.CalcUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class UserRankInfoClient {
	private BriefUserInfoClient user;
	private String nick;
	private String image;
	private String country;
	private BriefGuildInfoClient bgic;
	private long rankData;

	public void setBgic(BriefGuildInfoClient bgic) {
		this.bgic = bgic;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public void setRankData(long rankData) {
		this.rankData = rankData;
	}

	public void setUser(BriefUserInfoClient user) {
		this.user = user;
	}

	public BriefGuildInfoClient getBgic() {
		return bgic;
	}

	public String getCountry() {
		return country;
	}

	public String getImage() {
		return image;
	}

	public String getNick() {
		return nick;
	}

	public Long getRankData() {
		return rankData;
	}

	public BriefUserInfoClient getUser() {
		return user;
	}

	public static UserRankInfoClient convert(UserRankInfo uri,
			BriefUserInfoClient user, BriefGuildInfoClient bgic) {
		UserRankInfoClient uric = new UserRankInfoClient();

		uric.setUser(user);
		uric.setNick(uri.getNick());
		uric.setImage(BytesUtil.getLong(uri.getUserid(), uri.getImage())
				+ ".png");
		Country country = CacheMgr.countryCache.getCountry(uri.getCountry());
		uric.setCountry(country.getName());
		uric.setRankData(uri.getRankData());
		uric.setBgic(bgic);

		return uric;
	}

	public String getTitle() {
		if (StringUtil.isNull(country))
			return nick;
		return nick + "(" + country + ")";
	}

	public String getDesc(RankProp rp) {
		switch (rp.getId()) {
		case 1:
			UserVip vip = CacheMgr.userVipCache.getVipByCharge((int) rankData);
			return "VIP等级: " + "#player_vip#"
					+ StringUtil.numImgStr("v", vip.getLevel());
		case 3:
			return "总杀敌: #arm#" + CalcUtil.unitNum(rankData);
		case 4:
			return "等级: " + "#lv#" + StringUtil.numImgStr("v", (int) rankData);
		default:
			return "";
		}
	}
}
