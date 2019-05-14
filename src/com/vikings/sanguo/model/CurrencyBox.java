package com.vikings.sanguo.model;

import java.util.List;

import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.utils.StringUtil;

public class CurrencyBox {

	// 金箱子
	private static final int GOLD_BOX = 1;
	// 银箱子
	private static final int SILVER_BOX = 2;
	// 木箱子
	private static final int WOOD_BOX = 3;

	private int boxId;// 宝箱ID（Itemid）
	private int openBoxId;// 开宝箱需要物品id1
	private int openBoxIdCount;// 开宝箱需要物品id1数量（会扣除）
	private int holdItemCount;// 最多产生物品个数
	private int currencybox;// 元宝开启对应数量
	private int boxType;// 宝箱类型
	private String boxSpec;

	// 一下内容不是currency_box.csv里的配置项
	private Item expendItem;// 开宝箱需要物品
	private int itemCount;// 现有的物品

	// 宝箱排列顺序
	public static final int sortBox[] = { GOLD_BOX, SILVER_BOX, WOOD_BOX };

	public static CurrencyBox getCurrencyBoxByType(final List<CurrencyBox> cbs,
			int Type) {
		CurrencyBox cBox = new CurrencyBox();
		for (CurrencyBox currencyBox : cbs) {
			if (currencyBox.getBoxType() == Type) {
				cBox = currencyBox;
			}
		}
		return cBox;
	}

	public static String getBoxTypeName(int boxType) {
		switch (boxType) {
		case GOLD_BOX:
			return "金箱子";
		case SILVER_BOX:
			return "银箱子";
		case WOOD_BOX:
			return "木箱子";
		default:
			break;
		}
		return "未知箱子";
	}

	// 消耗的道具是否足够
	public boolean itemEnough() {
		if (openBoxIdCount <= itemCount) {
			return true;
		}
		return false;
	}

	// 消耗的元宝是否足够
	public boolean currencyEnough() {
		if (Account.user.getCurrency() >= currencybox) {
			return true;
		}
		return false;
	}

	public String getBoxSpec() {
		return boxSpec;
	}

	public void setBoxSpec(String boxSpec) {
		this.boxSpec = boxSpec;
	}

	public Item getExpendItem() {
		return expendItem;
	}

	public void setExpendItem(Item expendItem) {
		this.expendItem = expendItem;
	}

	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}

	public int getBoxId() {
		return boxId;
	}

	public void setBoxId(int boxId) {
		this.boxId = boxId;
	}

	public int getOpenBoxId() {
		return openBoxId;
	}

	public void setOpenBoxId(int openBoxId) {
		this.openBoxId = openBoxId;
	}

	public int getOpenBoxIdCount() {
		return openBoxIdCount;
	}

	public void setOpenBoxIdCount(int openBoxIdCount) {
		this.openBoxIdCount = openBoxIdCount;
	}

	public int getHoldItemCount() {
		return holdItemCount;
	}

	public void setHoldItemCount(int holdItemCount) {
		this.holdItemCount = holdItemCount;
	}

	public int getCurrencybox() {
		return currencybox;
	}

	public void setCurrencybox(int currencybox) {
		this.currencybox = currencybox;
	}

	public int getBoxType() {
		return boxType;
	}

	public void setBoxType(int boxType) {
		this.boxType = boxType;
	}

	public static int getGoldBox() {
		return GOLD_BOX;
	}

	public static int getSilverBox() {
		return SILVER_BOX;
	}

	public static int getWoodBox() {
		return WOOD_BOX;
	}

	public static CurrencyBox fromString(String line) {
		StringBuilder buf = new StringBuilder(line);
		CurrencyBox cb = new CurrencyBox();
		cb.setBoxId(StringUtil.removeCsvInt(buf));
		cb.setOpenBoxId(StringUtil.removeCsvInt(buf));
		cb.setOpenBoxIdCount(StringUtil.removeCsvInt(buf));
		cb.setHoldItemCount(StringUtil.removeCsvInt(buf));
		cb.setCurrencybox(StringUtil.removeCsvInt(buf));
		cb.setBoxType(StringUtil.removeCsvInt(buf));
		cb.setBoxSpec(StringUtil.removeCsv(buf));
		try {
			cb.setExpendItem((Item) CacheMgr.itemCache.get(cb.getOpenBoxId()));
		} catch (GameException e) {
			e.printStackTrace();
		}
		return cb;
	}

	// 更新现有的消耗品
	public CurrencyBox updataItemCount() {
		ItemBag bag = Account.store.getItemBag(this.getOpenBoxId());
		this.setItemCount(bag == null ? 0 : bag.getCount());
		return this;
	}
}
