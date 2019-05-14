/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-6-7 下午12:13:48
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.vikings.sanguo.model.ManorLocation;

public class ManorLocationCache extends FileCache {

	@Override
	public String getName() {
		return "manor_location.csv";
	}

	@Override
	public Object getKey(Object obj) {
		return ((ManorLocation) obj).getType();
	}

	@Override
	public Object fromString(String line) {
		return ManorLocation.fromString(line);
	}

	public List<ManorLocation> getAll() {
		List<ManorLocation> list = new ArrayList<ManorLocation>();
		Set<Entry<Integer, ManorLocation>> set = content.entrySet();
		for (Entry<Integer, ManorLocation> entry : set) {
			list.add(entry.getValue());
		}
		return list;
	}

	public ManorLocation getManorLocationByType(int buildingType) {
		ManorLocation mlLocation = new ManorLocation();
		List<ManorLocation> list = getAll();
		for (ManorLocation manorLocation : list) {
			if (manorLocation.getType() == buildingType) {
				mlLocation = manorLocation;
				break;
			}
		}
		return mlLocation;
	}

}
