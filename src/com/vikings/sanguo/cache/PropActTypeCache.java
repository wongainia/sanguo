/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-4-23 下午2:36:36
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.vikings.sanguo.model.PropActType;

public class PropActTypeCache extends FileCache {
	public static String FILE_NAME = "prop_act_type.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((PropActType) obj).getType();
	}

	@Override
	public Object fromString(String line) {
		return PropActType.fromString(line);
	}

	public PropActType getActTypeByType(int type) {
		List<PropActType> lActTypes = getAllData();
		for (int i = 0; i < lActTypes.size(); i++) {
			if (lActTypes.get(i).getType() == type) {
				return lActTypes.get(i);
			}
		}
		return null;
	}

	public List<PropActType> getAllData() {
		List<PropActType> ls = new ArrayList<PropActType>();
		Iterator<Entry<Integer, PropActType>> it = content.entrySet()
				.iterator();
		while (it.hasNext()) {
			Entry<Integer, PropActType> entry = it.next();
			PropActType ac = entry.getValue();
			if (Account.user.getLevel() >= ac.getOpenLvl())
				ls.add(ac);
		}

		Collections.sort(ls, new Comparator<PropActType>() {
			@Override
			public int compare(PropActType lhs, PropActType rhs) {
				return lhs.getType() - rhs.getType();
			}
		});
		return ls;
	}

	public List<PropActType> getShowPropAct() {
		List<PropActType> ls = getAllData();
		List<PropActType> temp = new ArrayList<PropActType>();
		for (PropActType propActType : ls) {
			if (propActType.isShow() && !temp.contains(propActType)) {
				temp.add(propActType);
			}
		}
		return temp;
	}

}
