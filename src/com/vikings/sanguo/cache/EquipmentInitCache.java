package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.EquipmentInit;

public class EquipmentInitCache extends FileCache {

	private static final String FILE_NAME = "equipment_init.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((EquipmentInit)obj).getScheme();
	}

	@Override
	public Object fromString(String line) {
		return EquipmentInit.fromString(line);
	}

}
