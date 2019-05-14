package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.GuildLevelUpMaterial;

public class GuildLevelUpMaterialCache extends ArrayFileCache {

	private static final String FILE_NAME = "guild_level_up_material.csv";

	@Override
	public long getSearchKey1(Object obj) {
		return ((GuildLevelUpMaterial) obj).getLevel();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return ((GuildLevelUpMaterial) obj).getItemId();
	}

	@Override
	public Object fromString(String line) {
		return GuildLevelUpMaterial.fromString(line);
	}

	@Override
	public String getName() {
		return FILE_NAME;
	}

}
