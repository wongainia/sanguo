package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.vikings.sanguo.model.PropTroopDesc;

public class PropTroopDescCache extends FileCache {

	private static final String FILE_NAME = "prop_troop_desc.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((PropTroopDesc) obj).getId();
	}

	@Override
	public Object fromString(String line) {
		return PropTroopDesc.fromString(line);
	}

	@SuppressWarnings("unchecked")
	public List<PropTroopDesc> getAll() {
		List<PropTroopDesc> list = new ArrayList<PropTroopDesc>();
		list.addAll(content.values());
		Collections.sort(list, new Comparator<PropTroopDesc>() {

			@Override
			public int compare(PropTroopDesc p1, PropTroopDesc p2) {
				return p1.getId() - p2.getId();
			}
		});
		return list;
	}
}
