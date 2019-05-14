package com.vikings.sanguo.cache;

import java.util.List;

import com.vikings.sanguo.model.HeroType;

public class HeroTypeCache extends ArrayFileCache {

	private static final String FILE_NAME = "hero_type.csv";

	@Override
	public long getSearchKey1(Object obj) {
		return ((HeroType) obj).getType();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return ((HeroType) obj).getStar();
	}

	@Override
	public Object fromString(String line) {
		return HeroType.fromString(line);
	}

	@Override
	public String getName() {
		return FILE_NAME;
	}

	public HeroType getHeroType(int type, int star) {
		List<HeroType> list = search(type);
		for (HeroType heroType : list) {
			if (heroType.getStar() == star)
				return heroType;
		}
		return null;
	}

	public HeroType getHeroTypeNextStar(int type, int star) {
		List<HeroType> list = search(type);
		for (HeroType heroType : list) {
			if (heroType.getStar() == star + 1)
				return heroType;
		}
		return null;
	}
}
