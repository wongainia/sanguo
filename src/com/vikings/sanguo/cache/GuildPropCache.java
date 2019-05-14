package com.vikings.sanguo.cache;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.vikings.sanguo.model.GuildProp;

public class GuildPropCache extends FileCache {
	private static final String FILE_NAME = "guild_prop.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((GuildProp) obj).getLevel();
	}

	public GuildProp search(int level) {
		List<GuildProp> guildProps = getAll();
		for (GuildProp gProp : guildProps) {
			if (gProp.getLevel() == level) {
				return gProp;
			}
		}
		return null;
	}

	@Override
	public Object fromString(String line) {
		return GuildProp.fromString(line);
	}

	@SuppressWarnings("unchecked")
	public GuildProp getGuildProp(int count) {
		Set<Entry<Short, GuildProp>> set = content.entrySet();
		for (Entry<Short, GuildProp> entry : set) {
			GuildProp guildProp = entry.getValue();
			if (count <= guildProp.getMaxMemberCnt()) {
				return guildProp;
			}
		}
		return null;
	}

}
