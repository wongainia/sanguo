package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.PokerPrice;

public class PokerPriceCache extends LazyLoadCache {
	private static final String FILE_NAME = "poker_price.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	public int getSize()
	{
		checkLoad();
		return content.size();
	}
	@Override
	public Object getKey(Object obj) {
		return ((PokerPrice) obj).getTimes();
	}

	@Override
	public Object fromString(String line) {
		return PokerPrice.fromString(line);
	}

}
