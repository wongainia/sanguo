package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.vikings.sanguo.model.HeroAbandonExpToItem;

public class HeroAbandonExpToItemCache extends FileCache {
	private static final String FILE_NAME = "hero_abandon_exp_to_item.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((HeroAbandonExpToItem) obj).getExp();
	}

	@Override
	public Object fromString(String line) {
		return HeroAbandonExpToItem.fromString(line);
	}

	@SuppressWarnings("unchecked")
	public List<HeroAbandonExpToItem> getAll() {
		Set<Entry<Integer, HeroAbandonExpToItem>> set = content.entrySet();
		List<HeroAbandonExpToItem> list = new ArrayList<HeroAbandonExpToItem>();
		for (Entry<Integer, HeroAbandonExpToItem> entry : set) {
			list.add(entry.getValue());
		}
		Collections.sort(list, new Comparator<HeroAbandonExpToItem>() {

			@Override
			public int compare(HeroAbandonExpToItem obj1,
					HeroAbandonExpToItem obj2) {
				return obj2.getExp() - obj1.getExp();
			}
		});
		return list;
	}

	@SuppressWarnings("unchecked") 
	public HeroAbandonExpToItem getByItemId(int itemId) {
		Set<Entry<Integer, HeroAbandonExpToItem>> set = content.entrySet();
		for (Entry<Integer, HeroAbandonExpToItem> entry : set) {
			if (entry.getValue().getItemId() == itemId)
				return entry.getValue();
		}
		return null;
	}
}
