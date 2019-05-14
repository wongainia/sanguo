package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.vikings.sanguo.model.BonusProp;

public class BonusCache extends FileCache {

	public static String FILE_NAME = "prop_bonus_client.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((BonusProp) obj).getId();
	}

	@Override
	public Object fromString(String line) {
		return BonusProp.fromString(line);
	}

	@SuppressWarnings("unchecked")
	public List<BonusProp> all() {
		List<BonusProp> ls = new ArrayList<BonusProp>();
		ls.addAll(content.values());
		Collections.sort(ls);
		return ls;
	}

	public BonusProp getBySpecialType(int specialType) {
		List<BonusProp> list = all();
		for (BonusProp prop : list) {
			if (prop.getSpecialType() == specialType)
				return prop;
		}
		return null;
	}

}
