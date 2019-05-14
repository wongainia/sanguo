package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class HeroDevourExp {
	private int heroId; // 将领id
	private int exp; // 提供经验值

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public static HeroDevourExp fromString(String csv) {
		HeroDevourExp heroDevourExp = new HeroDevourExp();
		StringBuilder buf = new StringBuilder(csv);
		heroDevourExp.setHeroId(StringUtil.removeCsvInt(buf));
		heroDevourExp.setExp(StringUtil.removeCsvInt(buf));
		return heroDevourExp;
	}
}
