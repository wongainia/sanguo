package com.vikings.sanguo.model;

import android.util.Log;

import com.vikings.sanguo.utils.StringUtil;

/**
 * 翻牌的价值
 * 
 * @author susong
 * 
 */
public class PokerPrice {
	private int times; // 翻牌次数
	private int price; // 价格（元宝）

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public static PokerPrice fromString(String csv) {
		PokerPrice pokePrice = new PokerPrice();
		StringBuilder buf = new StringBuilder(csv);
		pokePrice.setTimes(Integer.valueOf(StringUtil.removeCsv(buf)));
		pokePrice.setPrice(Integer.valueOf(StringUtil.removeCsv(buf)));
		return pokePrice;
	}
}
