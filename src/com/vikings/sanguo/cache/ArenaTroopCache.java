package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.vikings.sanguo.model.ArenaTroop;

public class ArenaTroopCache extends FileCache {

	public static String FILE_NAME = "arena_troop.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((ArenaTroop) obj).getId();
	}

	@Override
	public Object fromString(String line) {
		ArenaTroop troop = ArenaTroop.fromString(line);
		return troop;
	}

	@SuppressWarnings("unchecked")
	public List<ArenaTroop> getAll() {
		List<ArenaTroop> troops = new ArrayList<ArenaTroop>();
		Set<Entry<Integer, ArenaTroop>> set = content.entrySet();
		for (Entry<Integer, ArenaTroop> entry : set) {
			troops.add(entry.getValue());
		}
		Collections.sort(troops);
		return troops;
	}
}
