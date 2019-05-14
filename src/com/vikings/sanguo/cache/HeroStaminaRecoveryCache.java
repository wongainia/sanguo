package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.HeroStaminaRecovery;

public class HeroStaminaRecoveryCache extends FileCache {
	private static final String FILE_NAME = "hero_stamina_recovery.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((HeroStaminaRecovery) obj).getType();
	}

	@Override
	public Object fromString(String line) {
		return HeroStaminaRecovery.fromString(line);
	}

}
