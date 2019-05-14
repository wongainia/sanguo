package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.vikings.sanguo.model.EquipmentLevelUp;

public class EquipmentLevelUpCache extends FileCache {

	private static final String FILE_NAME = "equipment_levelup.csv";

	@Override
	public Object fromString(String line) {
		return EquipmentLevelUp.fromString(line);
	}

	public List<EquipmentLevelUp> getLevelUp(int scheme, byte quality,
			byte level) {
		List<EquipmentLevelUp> list = new ArrayList<EquipmentLevelUp>();
		Set<Entry<Short, EquipmentLevelUp>> set = content.entrySet();
		for (Entry<Short, EquipmentLevelUp> entry : set) {
			EquipmentLevelUp equipmentLevelUp = entry.getValue();
			if (equipmentLevelUp.getScheme() == scheme
					&& equipmentLevelUp.getQuality() == quality
					&& equipmentLevelUp.getMinLv() <= level
					&& equipmentLevelUp.getMaxLv() >= level) {
				list.add(equipmentLevelUp);
			}
		}
		return list;
	}

	public List<EquipmentLevelUp> getLevelUp2Top(int scheme, byte quality,
			byte level) {
		List<EquipmentLevelUp> list = new ArrayList<EquipmentLevelUp>();
		Set<Entry<Short, EquipmentLevelUp>> set = content.entrySet();
		for (int i = level; i < EquipmentLevelUp.EQUIPMENT_MAX_LEVEL; i++) {
			for (Entry<Short, EquipmentLevelUp> entry : set) {
				EquipmentLevelUp equipmentLevelUp = entry.getValue();
				if (equipmentLevelUp.getScheme() == scheme
						&& equipmentLevelUp.getQuality() == quality
						&& equipmentLevelUp.getMinLv() <= i
						&& equipmentLevelUp.getMaxLv() >= i) {
					list.add(equipmentLevelUp);
				}
			}
		}

		return list;
	}

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((EquipmentLevelUp) obj).getKey();
	}

}
