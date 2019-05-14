package com.vikings.sanguo.cache;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.Item;

public class ItemCache extends FileCache {

	private static final String FILE_NAME = "prop_item.csv";

	private HashMap<Integer, List<Item>> catalog = new HashMap<Integer, List<Item>>();

	@Override
	public void init() throws GameException {
		super.init();
		for (Object id : content.keySet()) {
			Item item = (Item) content.get(id);
			int type = item.getClentType();
			getByType(type).add(item);
		}
	}

	public List<Item> getByType(int type) {
		if (!catalog.containsKey(type))
			catalog.put(type, new ArrayList<Item>());
		return catalog.get(type);
	}

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object fromString(String line) {
		return Item.fromString(line);
	}

	@Override
	public Object getKey(Object obj) {
		return ((Item) obj).getId();
	}
	
	public List<Item> getIllustrations() {
		List<Item> illustrations = new ArrayList<Item>();
		
		Iterator<Item> it = content.entrySet().iterator();
		while (it.hasNext()) {
			Entry entry = (Entry) it.next();
			Item item = (Item) entry.getValue();
			if (item.isIllustastion())
				illustrations.add(item);
		}
		
		Collections.sort(illustrations, new Comparator<Item>() {
			@Override
			public int compare(Item lhs, Item rhs) {
				return lhs.getIllustrationIdx() - rhs.getIllustrationIdx();
			}
		});
		
		return illustrations;
	}
}
