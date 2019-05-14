package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.PropUser;

public class PropUserCache extends FileCache {

	private static final String FILE_NAME = "prop_user.csv";

	@Override
	public Object fromString(String line) {
		return PropUser.fromString(line);
	}

	@Override
	public Object getKey(Object obj) {
		return ((PropUser) obj).getLevel();
	}

	@Override
	public String getName() {
		return FILE_NAME;
	}

	public List<Integer> getRewardsIds(int preLv, int newLv) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = preLv + 1; i <= newLv; i++) {
			try {
				PropUser prop = (PropUser) get(i);
				list.add(prop.getRewardsItemId());
			} catch (GameException e) {

			}
		}
		return list;
	}

}
