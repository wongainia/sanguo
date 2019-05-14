package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.List;
import com.vikings.sanguo.model.CurrencyBox;

public class CurrencyBoxCache extends FileCache {
	final static String NAME = "currency_box.csv";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((CurrencyBox) obj).getBoxId();
	}

	@Override
	public Object fromString(String line) {
		return CurrencyBox.fromString(line);
	}

	public int size() {
		return content.size();
	}

	public List<CurrencyBox> getAll() {
		Object[] keys = getSortedKey();
		List<CurrencyBox> ls = new ArrayList<CurrencyBox>();
		for (Object it : keys) {
			ls.add(((CurrencyBox) content.get(it)).updataItemCount());
		}
		return ls;
	}
}
