package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.HeroQuality;

public class HeroQualityCache extends FileCache {
	private static final String FILE_NAME = "hero_quality.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((HeroQuality) obj).getTalent();
	}

	@Override
	public Object fromString(String line) {
		return HeroQuality.fromString(line);
	}
}
