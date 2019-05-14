package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.vikings.sanguo.cache.CacheMgr;

/**
 * 用户仓库
 * 
 * @author Brad.Chen
 * 
 */
public class Storehouse {

	private HashMap<Integer, List<ItemBag>> store = new HashMap<Integer, List<ItemBag>>();

	// 仓库容积
	public static int getCapability() {
		return CacheMgr.dictCache.getDictInt(Dict.TYPE_TIP, 1);// 背包上限200
	}

	public void add(ItemBag bag) {
		int type = bag.getItem().getClentType();
		if (!store.containsKey(type))
			store.put(type, new ArrayList<ItemBag>());
		store.get(type).add(bag);
	}

	public void addItemPack(ItemBag bag) {
		int type = bag.getItem().getClentType();
		if (!store.containsKey(type))
			store.put(type, new ArrayList<ItemBag>());
		// 叠加 or append
		List<ItemBag> bags = get(type);
		for (Iterator<ItemBag> itor = bags.iterator(); itor.hasNext();) {
			ItemBag it = itor.next();
			if (it.getId() == bag.getId()) {
				it.setCount(it.getCount() + bag.getCount());
				if (it.getCount() <= 0)
					itor.remove();
				return;
			}
		}
		if (bag.getCount() > 0) {
			bags.add(bag);
		}
	}

	private void arrange(int type) {
		List<ItemBag> ls = store.get(type);
		for (Iterator<ItemBag> it = ls.iterator(); it.hasNext();) {
			ItemBag itemBag = (ItemBag) it.next();
			if (itemBag.getCount() <= 0) {
				it.remove();
			}
		}
	}

	// 资源礼包
	public List<ItemBag> getGift() {
		return get(Item.TYPE_GIFT);
	}

	// 资源道具
	public List<ItemBag> getTools() {
		return get(Item.TYPE_TOOLS);
	}

	// 將魂
	public List<ItemBag> getSoul() {
		return get(Item.TYPE_HERO_SOUL);
	}

	// 宝石
	public List<ItemBag> getStone() {
		return get(Item.TYPE_STONE);
	}

	// 装备镶嵌的宝石
	public List<ItemBag> getEquipStone() {
		List<ItemBag> stones = new ArrayList<ItemBag>();
		Set<Integer> ids = CacheMgr.equipmentInsertItemCache.getAllIds();
		for (ItemBag itemBag : getStone()) {
			if (ids.contains(itemBag.getItemId()))
				stones.add(itemBag);
		}
		return stones;
	}

	public List<ItemBag> get(int type) {
		if (!store.containsKey(type))
			store.put(type, new ArrayList<ItemBag>());
		arrange(type);
		return store.get(type);
	}

	/**
	 * 仓库是否有某物品
	 * 
	 * @param itemId
	 * @param type
	 * @return
	 */
	public boolean hasItem(int itemId, int type) {
		return hasItem(itemId, type, 1);
	}

	public boolean hasItem(int itemId, int type, int count) {
		List<ItemBag> bags = get(type);
		for (ItemBag bag : bags) {
			if (bag.getItemId() == itemId && bag.getCount() >= count)
				return true;
		}
		return false;
	}

	public boolean hasItem(int itemId) {
		for (Entry<Integer, List<ItemBag>> entry : store.entrySet()) {
			List<ItemBag> bags = get(entry.getKey());
			for (ItemBag bag : bags) {
				if (bag.getItemId() == itemId && bag.getCount() >= 1)
					return true;
			}
		}
		return false;
	}

	public boolean hasItemBag(ItemBag bag) {
		List<ItemBag> bags = get(bag.getItem().getClentType());
		return bags.contains(bag);
	}

	/**
	 * 根据物品寻找玩家已有的物品包
	 * 
	 * @param item
	 * @return
	 */
	public ItemBag getItemBag(Item item) {
		List<ItemBag> ls = get(item.getClentType());
		for (ItemBag bag : ls) {
			if (bag.getItemId() == item.getId())
				return bag;
		}
		return null;
	}

	public ItemBag getItemBag(int type, int itemId) {
		for (ItemBag itemBag : get(type)) {
			if (itemBag.getItemId() == itemId)
				return itemBag;

		}
		return null;
	}

	public ItemBag getItemBag(int itemId) {
		for (Entry<Integer, List<ItemBag>> entry : store.entrySet()) {
			List<ItemBag> list = entry.getValue();
			for (ItemBag itemBag : list) {
				if (itemBag.getItemId() == itemId)
					return itemBag;
			}
		}
		return null;
	}

	public ItemBag getItemBag(long bagId) {
		for (Entry<Integer, List<ItemBag>> entry : store.entrySet()) {
			List<ItemBag> list = entry.getValue();
			for (ItemBag itemBag : list) {
				if (itemBag.getId() == bagId)
					return itemBag;
			}
		}
		return null;
	}

	/**
	 * 仅在新手教程中用来清除客户端假数据
	 */
	public void clear() {
		store.clear();
	}

	/**
	 * 取当前仓库物品包数量 按策划需求去掉魂魄 魂魄不占用格子
	 * 
	 * @return
	 */
	public int getSize() {
		int size = 0;
		for (Integer type : store.keySet()) {
			if (type == Item.TYPE_HERO_SOUL)
				continue;
			size += store.get(type).size();
		}
		return size;
	}

}
