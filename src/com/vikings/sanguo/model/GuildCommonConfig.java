package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class GuildCommonConfig {
	private int count;// 邀请消耗元宝数

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public static GuildCommonConfig fromString(String csv) {
		GuildCommonConfig config = new GuildCommonConfig();
		StringBuilder buf = new StringBuilder(csv);
		config.setCount(Integer.valueOf(StringUtil.removeCsv(buf)));
		return config;
	}
}
