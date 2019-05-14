package com.vikings.sanguo.cache;

import java.util.Map.Entry;
import java.util.Set;

import com.vikings.sanguo.model.EquipmentForgeEffect;

public class EquipmentForgeEffectCache extends LazyLoadCache {

	private static final String FILE_NAME = "equipment_forge_effect.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((EquipmentForgeEffect) obj).getId();
	}

	@Override
	public Object fromString(String line) {
		return EquipmentForgeEffect.fromString(line);
	}

	public EquipmentForgeEffect getNextLevel(EquipmentForgeEffect skill) {
		if (null != skill) {
			Set<Entry<Integer, EquipmentForgeEffect>> set = getContent()
					.entrySet();
			for (Entry<Integer, EquipmentForgeEffect> entry : set) {
				if (entry.getValue().getType() == skill.getType()
						&& entry.getValue().getLevel() == skill.getLevel() + 1)
					return entry.getValue();

			}
		}
		return null;
	}

}
