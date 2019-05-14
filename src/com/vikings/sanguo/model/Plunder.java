package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class Plunder {
	public static final int MANOR_SAME_COUNTRY = 1;
	public static final int MANOR_DIF_COUNTRY = 2;
	public static final int RESOURCE_SAME_COUNTRY = 3;
	public static final int RESOURCE_DIF_COUNTRY = 4;

	private int type;
	private int rate;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public static Plunder fromString(String line) {
		Plunder plunder = new Plunder();
		StringBuilder buf = new StringBuilder(line);
		plunder.setType(StringUtil.removeCsvInt(buf));
		plunder.setRate(StringUtil.removeCsvInt(buf));
		return plunder;
	}
}
