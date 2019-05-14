package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.vikings.sanguo.model.GambleData;
import com.vikings.sanguo.model.PropStronger;

public class PropStrongerCache extends FileCache {

	private static final String FILE_NAME = "prop_stronger.csv";

	private static final int mul = 10000;

	@Override
	public Object fromString(String line) {
		return PropStronger.fromString(line);
	}

	@Override
	public Object getKey(Object obj) {
		PropStronger d = (PropStronger) obj;
		return d.getType() * mul + d.getStar();
	}

	@Override
	public String getName() {
		return FILE_NAME;
	}

	public PropStronger getPs(int type, int value) {
		int key = type * mul + value;
		if (content.containsKey(key))
			return ((PropStronger) content.get(key));
		return null;
	}

	public PropStronger[] getPs(int type, int[] value) {
		PropStronger[] ps = new PropStronger[value.length];
		for (int i = 0; i < value.length; i++)
			ps[i] = (((PropStronger) content.get(type * mul + value[i]))
					.updateQuestInfoClient());
		return ps;
	}

	@SuppressWarnings("unchecked")
	public List<PropStronger> getPsByMainKey(int type) {
		List<PropStronger> list = new ArrayList<PropStronger>();
		for (Iterator<Entry<Integer, PropStronger>> iter = content.entrySet()
				.iterator(); iter.hasNext();) {
			Entry<Integer, PropStronger> entry = iter.next();
			if (entry.getKey() / mul == type) {
				list.add(entry.getValue().updateQuestInfoClient());
			}
		}
		sort(list);
		return list;
	}

	private void sort(List<PropStronger> l) {
		if (l.size() > 0) {
			Collections.sort(l, new Comparator<Object>() {
				@Override
				public int compare(Object object1, Object object2) {
					PropStronger ps1 = (PropStronger) object1;
					PropStronger ps2 = (PropStronger) object2;
					return ps1.getStar() - ps2.getStar();
				}
			});
		}
	}
}
