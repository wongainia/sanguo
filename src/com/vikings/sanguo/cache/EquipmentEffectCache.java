package com.vikings.sanguo.cache;

import java.util.List;

import com.vikings.sanguo.model.EquipmentEffect;

public class EquipmentEffectCache extends ArrayFileCache {
	private static final String FILE_NAME = "equipment_effect.csv";

	@Override
	public long getSearchKey1(Object obj) {
		return buildKey(((EquipmentEffect) obj).getEquipmentId(),
				((EquipmentEffect) obj).getQuality());
	}

	@Override
	public long getSearchKey2(Object obj) {
		return buildKey(((EquipmentEffect) obj).getEffectType(),
				((EquipmentEffect) obj).getMinLv());
	}

	@Override
	public Object fromString(String line) {
		return EquipmentEffect.fromString(line);
	}

	public EquipmentEffect getEquipmentEffect(int equipmentId, byte quality,
			int level) {
		List<EquipmentEffect> ees = search(buildKey(equipmentId, quality));
		for (EquipmentEffect ee : ees) {
			if (ee.getMinLv() <= level && ee.getMaxLv() >= level) {
				return ee;
			}
		}
		return null;
	}

	@Override
	public String getName() {
		return FILE_NAME;
	}

}
