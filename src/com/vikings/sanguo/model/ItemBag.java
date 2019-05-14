package com.vikings.sanguo.model;

import java.io.Serializable;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.message.Constants;

/**
 * 物品包 建立user和item之间的联系 包括生效时间等， 不可叠加的物品 包裹内只有1个
 * 
 * @author Brad.Chen
 * 
 */
public class ItemBag implements Comparable<ItemBag>, Copyable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5135063968729984710L;

	public static final int RS_BAG_LENGTH = Constants.SHORT_LEN
			+ Constants.INT_LEN * 2 + Constants.LONG_LEN + 1;

	public static final byte SOURCE_NORMAL = 0;
	public static final byte SOURCE_LEVEL_UP = 1;

	public static final int RANDOM_ITEM = -1;

	private long id;

	private int count;

	private int itemId;

	private int source;

	private int buyTime;

	private transient boolean isNew = false;

	private transient boolean isLaidEesOn = false;

	public boolean isLaidEesOn() {
		return isLaidEesOn;
	}

	public void setLaidEesOn(boolean isLaidEesOn) {
		this.isLaidEesOn = isLaidEesOn;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	private transient Item item;//道具商品类

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getBuyTime() {
		return buyTime;
	}

	public void setBuyTime(int buyTime) {
		this.buyTime = buyTime;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public boolean isReward() {
		return this.source == com.vikings.sanguo.Constants.GIFT_ITEM;
	}

	//道具的过期时间
	public int getLeftDay() {
		if (item.getPeriod() == 0)
			return 0;
		else
			return item.getPeriod()
					- (int) (Config.serverTime() / 1000 - buyTime)
					/ (60 * 60 * 24);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemBag other = (ItemBag) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public int compareTo(ItemBag another) {
		if (another == null)
			return 1;
		else
			return itemId - another.itemId;
	}

	@Override
	public void copyFrom(Object another) {
		if (!(another instanceof ItemBag))
			return;
		ItemBag bag = (ItemBag) another;
		id = bag.id;
		count = bag.count;
		itemId = bag.itemId;
		// source = bag.source;
		if (bag.buyTime != 0)
			buyTime = bag.buyTime;
		item = bag.item;
	}

}
