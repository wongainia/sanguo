package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.vikings.sanguo.model.ItemEffect;

public class ItemEffectCache extends FileCache {
	private static final String FILE_NAME = "item_effect.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((ItemEffect) obj).toString();
	}

	@Override
	public Object fromString(String line) {
		return ItemEffect.fromString(line);
	}

	public List<ItemEffect> getByItemId(int itemId) {
		List<ItemEffect> effects = new ArrayList<ItemEffect>();
		Set<Entry<String, ItemEffect>> set = content.entrySet();
		for (Entry<String, ItemEffect> entry : set) {
			if (entry.getValue().getItemId() == itemId)
				effects.add(entry.getValue());
		}
		return effects;
	}
}
