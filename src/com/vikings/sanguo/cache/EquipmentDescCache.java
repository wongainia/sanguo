package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.EquipmentDesc;

public class EquipmentDescCache extends FileCache {
	public static String FILE_NAME = "equipment_desc.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((EquipmentDesc) obj).getQuality();
	}

	@Override
	public Object fromString(String line) {
		return EquipmentDesc.fromString(line);
	}

}
