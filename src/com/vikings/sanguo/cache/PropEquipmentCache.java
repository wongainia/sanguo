package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.PropEquipment;

public class PropEquipmentCache extends FileCache {
	public static String FILE_NAME = "prop_equipment.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((PropEquipment) obj).getId();
	}

	@Override
	public Object fromString(String line) {
		return PropEquipment.fromString(line);
	}

}
