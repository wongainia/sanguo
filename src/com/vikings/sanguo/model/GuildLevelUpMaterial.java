package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class GuildLevelUpMaterial {
	private int level; //等级
	private int itemId; //物品id
	private int count;//数量

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public static GuildLevelUpMaterial fromString(String csv) {
		GuildLevelUpMaterial material = new GuildLevelUpMaterial();
		StringBuilder buf = new StringBuilder(csv);
		material.setLevel(Integer.valueOf(StringUtil.removeCsv(buf)));
		material.setItemId(Integer.valueOf(StringUtil.removeCsv(buf)));
		material.setCount(Integer.valueOf(StringUtil.removeCsv(buf)));
		return material;
	}
}
