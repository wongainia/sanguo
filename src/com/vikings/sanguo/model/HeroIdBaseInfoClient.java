package com.vikings.sanguo.model;

import java.io.Serializable;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.utils.StringUtil;

public class HeroIdBaseInfoClient implements Serializable {
	private static final long serialVersionUID = 8524997827757656368L;

	protected static final byte DEFAULT_LEVEL = 60;
	protected static final byte TALENT_CHANGE_CLOTH = 5;

	protected long id;
	protected int heroId;
	protected int star;
	protected int talent;

	protected HeroProp heroProp;
	protected HeroQuality heroQuality;// 将领品质显示相关
	protected HeroType heroType;//

	public HeroIdBaseInfoClient(long id, int heroId, int star, int talent)
			throws GameException {
		this.id = id;
		this.heroId = heroId;
		this.star = star;
		this.talent = talent;
		this.heroProp = (HeroProp) CacheMgr.heroPropCache.get(heroId);
		if (talent > 0) {
			this.heroQuality = (HeroQuality) CacheMgr.heroQualityCache
					.get((byte) talent);
		}
		if (star > 0) {
			this.heroType = CacheMgr.heroTypeCache.getHeroType(talent, star);
		}
	}

	protected HeroIdBaseInfoClient() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public int getTalent() {
		return talent;
	}

	public void setTalent(int talent) {
		this.talent = talent;
	}

	public boolean isMaxTalent() {
		return talent == heroProp.getMaxTalent();
	}

	public boolean isMaxStar() {
		return star == Constants.HERO_MAX_STAR;
	}

	public boolean isWorldLevelLimit() {
		return null != WorldLevelInfoClient.getWorldLevelProp()
				&& getTalent() == WorldLevelInfoClient.getWorldLevelProp()
						.getMaxHeroTalent();
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public String getStarName() {
		return getName(star);
	}

	public String getNextStarName() {
		return getName(star + 1);
	}

	private String getName(int star) {
		String name = "";
		switch (star) {
		case 1:
			name = "一星";
			break;
		case 2:
			name = "二星";
			break;
		case 3:
			name = "三星";
			break;
		case 4:
			name = "四星";
			break;
		case 5:
			name = "五星";
			break;
		case 6:
			name = "六星";
			break;

		default:
			break;
		}
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (null == o)
			return false;
		if (o instanceof HeroIdBaseInfoClient) {
			return id == ((HeroIdBaseInfoClient) o).getId();
		}
		return false;
	}

	public HeroProp getHeroProp() {
		return this.heroProp;
	}

	public void setHeroProp(HeroProp heroProp) {
		this.heroProp = heroProp;
	}

	public HeroQuality getHeroQuality() {
		return heroQuality;
	}

	public void setHeroQuality(HeroQuality heroQuality) {
		this.heroQuality = heroQuality;
	}

	public HeroType getHeroType() {
		return heroType;
	}

	public void setHeroType(HeroType heroType) {
		this.heroType = heroType;
	}

	public String getColorHeroName() {
		return StringUtil.getHeroName(heroProp, heroQuality);
	}

	public String getColorTypeName() {
		return StringUtil.getHeroTypeName(heroQuality);
	}

	public String getColorTypeStar() {
		return StringUtil.color(heroQuality.getName() + " " + getStar() + "星",
				heroQuality.getColor());
	}

	public String getHeroFullName() {
		return getColorTypeName() + " " + getColorHeroName();
	}

	public String getColor() {
		String color = "#F0FAFF";
		if (null != heroQuality)
			color = heroQuality.getColor();
		return color;
	}

	public byte getEvolveLevel() {
		return null != heroType ? heroType.getEvolveLevel() : DEFAULT_LEVEL;
	}
}
