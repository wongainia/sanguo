/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2014-4-1 上午11:38:56
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.vikings.sanguo.cache.FileCache;

public class PropHeroFavourCache extends FileCache {

	private static final String FILE_NAME = "prop_hero_favour.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((PropHeroFavour) obj).getId();
	}

	@Override
	public Object fromString(String line) {
		return PropHeroFavour.fromString(line);
	}

	@SuppressWarnings("unchecked")
	public List<PropHeroFavour> getAll() {
		List<PropHeroFavour> list = new ArrayList<PropHeroFavour>();
		list.addAll(content.values());
		Collections.sort(list, new Comparator<PropHeroFavour>() {

			@Override
			public int compare(PropHeroFavour object1, PropHeroFavour object2) {
				return object1.getId() - object2.getId();
			}

		});
		return list;
	}

}
