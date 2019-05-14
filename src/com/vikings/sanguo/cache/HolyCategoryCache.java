/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-11-8 上午9:53:45
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.model.HolyCategory;

public class HolyCategoryCache extends FileCache{
	final static String NAME = "holy_category.csv";
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((HolyCategory)obj).getId();
	}

	@Override
	public Object fromString(String line) {
		return HolyCategory.fromString(line);
	}

	public int size() {
		return content.size();
	}
	
	public List<HolyCategory> getAll() {
		Object[] keys = getSortedKey();
		List<HolyCategory> ls = new ArrayList<HolyCategory>();
		for (Object it : keys) 
			ls.add((HolyCategory) content.get(it));
		return ls;
	}
}
