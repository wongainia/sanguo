package com.vikings.sanguo.cache;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.DoubleCharge;

public class DoubleChargeCache extends FileCache {

	private static final String FILE_NAME = "double_charge.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((DoubleCharge) obj).getTimes();
	}

	@Override
	public Object fromString(String line) {
		return DoubleCharge.fromString(line);
	}

	/**
	 * @param curTimes
	 *            当前已经领过的次数
	 * @return
	 */
	public int getDoubleRechargeTotal(int curTimes) {
		try {
			DoubleCharge charge = (DoubleCharge) get(curTimes + 1);
			return charge.getTotal();
		} catch (GameException e) {
			e.printStackTrace();
		}
		return 100;
	}
}
