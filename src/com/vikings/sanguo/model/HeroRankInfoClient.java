package com.vikings.sanguo.model;

import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.HeroRankInfo;
import com.vikings.sanguo.utils.StringUtil;

public class HeroRankInfoClient {
	private long id;
	private int heroid;
	private HeroProp heroProp;
	private HeroQuality heroQuality;
	private int maxArmValue;
	private int star;
	private byte talent;
	private int lord;
	private String lordNick;
	private String country;
	private BriefGuildInfoClient guild;

	public void setCountry(String country) {
		this.country = country;
	}

	public void setGuild(BriefGuildInfoClient guild) {
		this.guild = guild;
	}

	public void setHeroid(int heroid) {
		this.heroid = heroid;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setLord(int lord) {
		this.lord = lord;
	}

	public void setLordNick(String lordNick) {
		this.lordNick = lordNick;
	}

	public void setMaxArmValue(int maxArmValue) {
		this.maxArmValue = maxArmValue;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public byte getTalent() {
		return talent;
	}

	public void setTalent(byte talent) {
		this.talent = talent;
	}

	public void setHeroProp(HeroProp heroProp) {
		this.heroProp = heroProp;
	}

	public void setHeroQuality(HeroQuality heroQuality) {
		this.heroQuality = heroQuality;
	}

	public String getCountry() {
		return country;
	}

	public BriefGuildInfoClient getGuild() {
		return guild;
	}

	public int getHeroid() {
		return heroid;
	}

	public long getId() {
		return id;
	}

	public int getLord() {
		return lord;
	}

	public String getLordNick() {
		return lordNick;
	}

	public String getLordName() {
		if (StringUtil.isNull(country))
			return lordNick;
		return lordNick + "  (" + country + ")";
	}

	public int getMaxArmValue() {
		return maxArmValue;
	}

	public int getStar() {
		return star;
	}

	public HeroProp getHeroProp() {
		return heroProp;
	}

	public HeroQuality getHeroQuality() {
		return heroQuality;
	}

	public static HeroRankInfoClient convert(HeroRankInfo hri,
			BriefGuildInfoClient bgic) throws GameException {
		HeroRankInfoClient hric = new HeroRankInfoClient();

		hric.setId(hri.getId());
		hric.setHeroid(hri.getHeroid());

		hric.setHeroProp((HeroProp) CacheMgr.heroPropCache.get(hri.getHeroid()));
		hric.setTalent((byte) hri.getTalent().intValue());
		hric.setHeroQuality((HeroQuality) CacheMgr.heroQualityCache
				.get((byte) hri.getTalent().intValue()));

		hric.setMaxArmValue(hri.getMaxArmValue());
		hric.setStar(hri.getType());
		hric.setLord(hri.getLord());
		hric.setLordNick(hri.getLordNick());

		Country country = CacheMgr.countryCache.getCountry(hri.getCountry());
		hric.setCountry(country.getName());

		hric.setGuild(bgic);

		return hric;
	}
}
