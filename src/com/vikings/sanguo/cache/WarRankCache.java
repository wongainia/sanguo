package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.vikings.sanguo.model.WarRankProp;

public class WarRankCache extends FileCache {
	private static final String FILE_NAME = "prop_warrank.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((WarRankProp) obj).getId();
	}

	@Override
	public Object fromString(String line) {
		return WarRankProp.fromString(line);
	}

	@SuppressWarnings("unchecked")
	public List<WarRankProp> getAll() {
		List<WarRankProp> list = new ArrayList<WarRankProp>();
		Set<Entry<Integer, WarRankProp>> entrySet = content.entrySet();
		for (Entry<Integer, WarRankProp> entry : entrySet) {
			if (entry.getValue().getRank() > 0)
				list.add(entry.getValue());
		}
		return list;
	}

	public WarRankProp getNextRank(int rank) {
		for (WarRankProp warRankProp : getAll()) {
			if (rank + 1 == warRankProp.getRank()) {
				return warRankProp;
			}
		}
		return null;
	}
}
