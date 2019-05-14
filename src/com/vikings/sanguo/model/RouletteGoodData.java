package com.vikings.sanguo.model;

public class RouletteGoodData extends SelectData {
	private int itemId; // 物品id
	private PropRouletteItem prop;
	private ItemBag itemBag;

	private int max;

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public PropRouletteItem getProp() {
		return prop;
	}

	public void setProp(PropRouletteItem prop) {
		this.prop = prop;
	}

	public ItemBag getItemBag() {
		return itemBag;
	}

	public void setItemBag(ItemBag itemBag) {
		this.itemBag = itemBag;
		max = itemBag.getCount();
	}

	public int getMax() {
		return max;
	}

	public boolean reduce() {
		max = max - selCount;
		selCount = 0;
		return max > 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RouletteGoodData other = (RouletteGoodData) obj;
		if (itemId != other.itemId)
			return false;
		return true;
	}

}
