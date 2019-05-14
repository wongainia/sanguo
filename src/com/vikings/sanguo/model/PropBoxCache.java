/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2014-4-12 下午3:40:04
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

public class PropBoxCache extends FileCache {

private static final String FILE_NAME = "prop_box.csv";
	
	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((PropBox) obj).getId();
	}

	@Override
	public Object fromString(String line) {
		return PropBox.fromString(line);
	}

	@SuppressWarnings("unchecked")
	public List<PropBox> getAll() {
		List<PropBox> list = new ArrayList<PropBox>();
		list.addAll(content.values());
		Collections.sort(list, new Comparator<PropBox>() {

			@Override
			public int compare(PropBox object1, PropBox object2) {
				return object1.getId() - object2.getId();
			}

		});
		return list;
	}

}
