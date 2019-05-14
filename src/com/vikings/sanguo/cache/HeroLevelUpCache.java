package com.vikings.sanguo.cache;

import java.util.List;

import com.vikings.sanguo.model.HeroLevelUp;

public class HeroLevelUpCache extends LazyLoadArrayCache {
	private static final String FILE_NAME = "hero_levelup.csv";

	@Override
	public long getSearchKey1(Object obj) {
		return buildKey(((HeroLevelUp) obj).getTalent(),
				((HeroLevelUp) obj).getStar());
	}

	@Override
	public long getSearchKey2(Object obj) {
		return ((HeroLevelUp) obj).getLevel();
	}

	@Override
	public Object fromString(String line) {
		return HeroLevelUp.fromString(line);
	}

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@SuppressWarnings("unchecked")
	public List<HeroLevelUp> searchExp(int type, int star) {
		return search(buildKey(type, star));
	}

	public HeroLevelUp getExp(int type, int star, int level) {
		return (HeroLevelUp) search(buildKey(type, star), level);
	}
}
