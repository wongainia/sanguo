package com.vikings.sanguo.cache;

import java.util.Random;

import com.vikings.sanguo.model.Tips;

public class TipsCache extends FileCache {

	private static final String FILE_NAME = "tips.csv";

	@Override
	public Object fromString(String line) {
		return Tips.fromString(line);
	}

	@Override
	public Object getKey(Object obj) {
		Tips tip = (Tips) obj;
		return tip.getTipId();
	}

	@Override
	public String getName() {
		return FILE_NAME;
	}

	public String getTips(int key) {
		if (content != null) {
			if (content.containsKey(key))
				return ((Tips) content.get(key)).getDesc();
		}
		return "";
	}

	public int getRandomNum() {
		if (content == null || content.size() == 0)
			return 1;
		Random rnd = new Random();
		if (content != null) {
			return rnd.nextInt(content.size()) + 1;
		}
		return 1;
	}
}
