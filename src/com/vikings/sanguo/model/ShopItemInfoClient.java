package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.ShopItemInfo;

public class ShopItemInfoClient {
	public static final int TYPE_TOOLS = 1;
	public static final int TYPE_EQUIP = 2;

	private int type;// 1道具 2装备
	private int id;
	private int count;
	private int timeScheme;// 时间方案 如无或为0表示终身

	public Item item;
	public PropEquipment equipment;

	public ShopItemInfoClient(int type, int id) throws GameException {
		this.type = type;
		this.id = id;
		if (type == TYPE_TOOLS)
			item = (Item) CacheMgr.itemCache.get(id);
		else if (type == TYPE_EQUIP)
			equipment = (PropEquipment) CacheMgr.propEquipmentCache.get(id);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getTimeScheme() {
		return timeScheme;
	}

	public void setTimeScheme(int timeScheme) {
		this.timeScheme = timeScheme;
	}

	public static ShopItemInfoClient convert(ShopItemInfo info)
			throws GameException {
		if (null == info)
			return null;
		ShopItemInfoClient siic = new ShopItemInfoClient(info.getType(),
				info.getId());
		siic.setCount(info.getCount());
		siic.setTimeScheme(info.getTimeScheme());
		return siic;
	}

	public static List<ShopItemInfoClient> convert2List(List<ShopItemInfo> infos)
			throws GameException {
		List<ShopItemInfoClient> list = new ArrayList<ShopItemInfoClient>();
		if (null != infos && !infos.isEmpty()) {
			for (ShopItemInfo info : infos) {
				ShopItemInfoClient siic = convert(info);
				if (null != siic)
					list.add(siic);
			}
		}
		return list;

	}
}
