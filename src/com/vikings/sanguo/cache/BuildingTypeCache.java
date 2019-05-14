package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.vikings.sanguo.model.BuildingType;

public class BuildingTypeCache extends FileCache {
	private static final String FILE_NAME = "building_type.csv";

	@Override
	public Object fromString(String line) {
		return BuildingType.fromString(line);
	}

	@Override
	public Object getKey(Object obj) {
		return ((BuildingType) obj).getType();
	}

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@SuppressWarnings("unchecked")
	public List<BuildingType> getAll() {
		List<BuildingType> list = new ArrayList<BuildingType>();
		list.addAll(content.values());
		return list;
	}

}
