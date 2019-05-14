package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.EquipmentForge;

public class EquipmentForgeCache extends ArrayFileCache {

	public static String FILE_NAME = "equipment_forge.csv";

	@Override
	public long getSearchKey1(Object obj) {
		return ((EquipmentForge) obj).getScheme();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return ((EquipmentForge) obj).getLevel();
	}

	@Override
	public Object fromString(String line) {
		return EquipmentForge.fromString(line);
	}

	@Override
	public String getName() {
		return FILE_NAME;
	}

}
