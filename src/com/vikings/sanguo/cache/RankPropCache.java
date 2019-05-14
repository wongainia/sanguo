/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-10-10 下午6:01:51
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
import com.vikings.sanguo.model.RankProp;

public class RankPropCache extends FileCache{
	private final static String NAME = "prop_rank.csv";
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((RankProp)obj).getId();
	}

	@Override
	public Object fromString(String line) {
		return RankProp.fromString(line);
	}

	public List<RankProp> getAllData() {
		List<RankProp> ls = new ArrayList<RankProp>();
		Iterator<Entry<Integer, RankProp>> it = content.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, RankProp> entry = it.next();
			RankProp rp = entry.getValue();
			ls.add(rp);
		}
		
		Collections.sort(ls, new Comparator<RankProp>() {
			@Override
			public int compare(RankProp lhs, RankProp rhs) {
				return lhs.getId() - rhs.getId();
			}
		});
		return ls;
	}
}
