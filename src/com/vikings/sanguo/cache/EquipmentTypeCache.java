package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.EquipmentType;

public class EquipmentTypeCache extends ArrayFileCache {
	public static String FILE_NAME = "equipment_type.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object fromString(String line) {
		return EquipmentType.fromString(line);
	}

	@Override
	public long getSearchKey1(Object obj) {
		return ((EquipmentType) obj).getPlanId();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return ((EquipmentType) obj).getQuality();
	}

}
