package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class DoubleCharge {
	private int times; // 次数
	private int total;// 双倍积分上限

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public static DoubleCharge fromString(String line) {
		DoubleCharge charge = new DoubleCharge();
		StringBuilder buf = new StringBuilder(line);
		charge.setTimes(StringUtil.removeCsvInt(buf));
		charge.setTotal(StringUtil.removeCsvInt(buf));
		return charge;
	}
}
