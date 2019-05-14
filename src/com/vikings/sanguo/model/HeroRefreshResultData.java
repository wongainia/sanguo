package com.vikings.sanguo.model;

public class HeroRefreshResultData {
	private HeroInfoClient hic;
	private ItemBag itemBag;

	public HeroRefreshResultData(HeroInfoClient hic) {
		this.hic = hic;
		this.itemBag = null;
	}

	public HeroRefreshResultData(ItemBag itemBag) {
		this.itemBag = itemBag;
		this.hic = null;
	}

	public HeroInfoClient getHic() {
		return hic;
	}

	public ItemBag getItemBag() {
		return itemBag;
	}

	public boolean isHero() {
		return null != hic;
	}

	public boolean isItem() {
		return null != itemBag;
	}

}
