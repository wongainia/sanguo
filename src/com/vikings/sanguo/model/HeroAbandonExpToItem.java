package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

//将领升级物品对应的经验
public class HeroAbandonExpToItem {
	private int exp;
	private int itemId;

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public static HeroAbandonExpToItem fromString(String line) {
		HeroAbandonExpToItem haeti = new HeroAbandonExpToItem();
		StringBuilder buf = new StringBuilder(line);
		haeti.setExp(StringUtil.removeCsvInt(buf));
		haeti.setItemId(StringUtil.removeCsvInt(buf));
		return haeti;
	}
}
