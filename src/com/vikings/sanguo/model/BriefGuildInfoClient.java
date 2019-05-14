package com.vikings.sanguo.model;

import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.protos.BriefGuildInfo;

public class BriefGuildInfoClient {
	protected int id; // 家族id
	protected String name; // 家族名称
	protected int image; // 徽章
	protected int leader; // 族长
	protected int level; // 等级
	protected Country country;
	protected int countryId;
	protected long altarId;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}

	public boolean hasImage() {
		return this.image > 1000;
	}

	public int getLeader() {
		return leader;
	}

	public void setLeader(int leader) {
		this.leader = leader;
	}

	public boolean isLeader(int userId) {
		return leader == userId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Country getCountry() {
		return country;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public void setAltarId(long altarsId) {
		this.altarId = altarsId;
	}
	
	public long getAltarId() {
		return altarId;
	}
	
	public static BriefGuildInfoClient convert(BriefGuildInfo info) {
		if (null == info)
			return null;
		BriefGuildInfoClient bgic = new BriefGuildInfoClient();
		bgic.setId(info.getId());
		bgic.setName(info.getName());
		bgic.setImage(info.getImage());
		bgic.setLeader(info.getLeader());
		bgic.setLevel(info.getLevel());
		bgic.setCountryId(info.getCountry());
		bgic.setCountry(CacheMgr.countryCache.getCountry(info.getCountry()));
		return bgic;
	}

	public boolean isMeLeader() {
		return leader == Account.user.getId();
	}
	
	public boolean hasAltar() {
		return altarId != 0;
	}
}
