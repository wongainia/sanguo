/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2014-4-2 下午5:12:33
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

public class HeroFavourWordCache extends FileCache {

	private static final String FILE_NAME = "hero_favour_words.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((HeroFavourWords) obj).getId();
	}

	@Override
	public Object fromString(String line) {
		return HeroFavourWords.fromString(line);
	}

	@SuppressWarnings("unchecked")
	public List<HeroFavourWords> getAll() {
		List<HeroFavourWords> list = new ArrayList<HeroFavourWords>();
		list.addAll(content.values());
		Collections.sort(list, new Comparator<HeroFavourWords>() {

			@Override
			public int compare(HeroFavourWords object1, HeroFavourWords object2) {
				return object1.getId() - object2.getId();
			}

		});
		return list;
	}
}
