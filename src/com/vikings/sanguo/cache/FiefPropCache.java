package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.vikings.sanguo.model.FiefProp;

public class FiefPropCache extends FileCache {

	private static final String FILE_NAME = "prop_fief.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((FiefProp) obj).getId();
	}

	@Override
	public Object fromString(String line) {
		return FiefProp.fromString(line);
	}

	public List<Integer> getAdvancedResPropIds(int resType, int minLvl) {
		List<FiefProp> fiefs = new ArrayList<FiefProp>();
		
		Iterator<Entry<Integer, FiefProp>> it = content.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, FiefProp> entry = it.next();
			FiefProp fp = entry.getValue();
			if (fp.getProductType() == resType && fp.getScaleId() >= minLvl)
				fiefs.add(fp);
		}
		
		Collections.sort(fiefs, new Comparator<FiefProp>() {

			@Override
			public int compare(FiefProp lhs, FiefProp rhs) {
				return rhs.getScaleId() - lhs.getScaleId();
			}
		});
		
		List<Integer> ids = new ArrayList<Integer>();
		for (FiefProp fp : fiefs) 
			ids.add(fp.getId());
		
		return ids;
	}
}
