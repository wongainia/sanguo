package com.vikings.sanguo.cache;

import java.util.Map.Entry;
import java.util.Set;

import com.vikings.sanguo.model.GuildCommonConfig;

public class GuildCommonConfigCache extends FileCache {

	private static final String FILE_NAME = "guild_common_config.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((GuildCommonConfig) obj).getCount();
	}

	@Override
	public Object fromString(String line) {
		return GuildCommonConfig.fromString(line);
	}

	//邀请消耗元宝
	public int getInviteRmbCost() {
		int count = 0;
		Set<Entry<Integer, GuildCommonConfig>> set = content.entrySet();
		for (Entry<Integer, GuildCommonConfig> entry : set) {
			if (null != entry.getValue()) {
				count = entry.getValue().getCount();
			}
		}
		return count;
	}
}
