package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.HeroSkillUpgrade;

public class HeroSkillUpgradeCache extends LazyLoadArrayCache {
	private static final String FILE_NAME = "hero_skill_upgrade.csv";

	@Override
	public long getSearchKey1(Object obj) {
		return ((HeroSkillUpgrade) obj).getSkillID();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return ((HeroSkillUpgrade) obj).getItemID();
	}

	@Override
	public Object fromString(String line) {
		return HeroSkillUpgrade.fromString(line);
	}

	@Override
	public String getName() {
		return FILE_NAME;
	}

}
