package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.SiteSpecial;

public class SiteSpecialCache extends ArrayFileCache {

	private static final String FILE_NAME = "site_special.csv";

	@Override
	public long getSearchKey1(Object obj) {
		return ((SiteSpecial) obj).getFiefPropId();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return ((SiteSpecial) obj).getBuildingPropId();
	}

	@Override
	public Object fromString(String line) {
		return SiteSpecial.fromString(line);
	}

	@Override
	public String getName() {
		return FILE_NAME;
	}

}
