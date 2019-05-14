package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.BuildingStatus;

public class BuildingStatusCache extends FileCache {
	private static final String FILE_NAME = "building_status.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((BuildingStatus) obj).getBuildingId();
	}

	@Override
	public Object fromString(String line) {
		return BuildingStatus.fromString(line);
	}

}
