/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-10-9 下午2:56:43
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import com.vikings.sanguo.protos.HonorRankInfo;

public class HonorRankData {
	private BriefUserInfoClient user;
	private BriefGuildInfoClient guild;
	private HonorRankInfo honorRank;
	private HeroProp heroProp;
	private HeroQuality heroQuality;
	private long heroId;
	private Item item;
	
	public void setGuild(BriefGuildInfoClient guild) {
		this.guild = guild;
	}

	public void setHeroProp(HeroProp heroProp) {
		this.heroProp = heroProp;
	}
	
	public void setHeroId(long heroId) {
		this.heroId = heroId;
	}
	
	public void setHeroQuality(HeroQuality heroQuality) {
		this.heroQuality = heroQuality;
	}
	
	public void setHonorRank(HonorRankInfo honorRank) {
		this.honorRank = honorRank;
	}
	
	public void setUser(BriefUserInfoClient user) {
		this.user = user;
	}
	
	public void setItem(Item item) {
		this.item = item;
	}
	
	public BriefGuildInfoClient getGuild() {
		return guild;
	}
	
	public long getHeroId() {
		return heroId;
	}
	
	public HeroProp getHeroProp() {
		return heroProp;
	}
	
	public HeroQuality getHeroQuality() {
		return heroQuality;
	}
	
	public HonorRankInfo getHonorRank() {
		return honorRank;
	}
	
	public BriefUserInfoClient getUser() {
		return user;
	}
	
	public Item getItem() {
		return item;
	}
}
