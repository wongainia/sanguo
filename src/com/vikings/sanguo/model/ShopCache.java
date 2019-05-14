package com.vikings.sanguo.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.util.Log;

import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.ArrayFileCache;

/**
 * 用户商店
 * 
 * @author Brad.Chen
 * 
 */
public class ShopCache extends ArrayFileCache {

	private static final String FILE_NAME = "shop.csv";

	@Override
	public long getSearchKey1(Object obj) {
		return ((ShopData) obj).getViewTab();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return buildKey(((ShopData) obj).getType(), ((ShopData) obj).getId());
	}

	@Override
	public Object fromString(String line) {
		return ShopData.fromString(line);
	}

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@SuppressWarnings("unchecked")
	public List<ShopData> getTabData(byte viewTab) {
		List<ShopData> list = search(viewTab);
		Collections.sort(list, new Comparator<ShopData>() {

			@Override
			public int compare(ShopData object1, ShopData object2) {
				return object1.getSequence() - object2.getSequence();
			}
		});
		return list;
	}

	public ShopData getShopDataById(int itemId) {
		List<ShopData> shopDatas = getAll();
		for (int i = 0; i < shopDatas.size(); i++) {
			if (shopDatas.get(i).getId() == itemId) {
				return shopDatas.get(i);
			}
		}
		return null;
	}

	public int getMinVipLvByTab(byte tab) {
		int minLv = -1;
		for (Object obj : list) {
			ShopData data = (ShopData) obj;
			if (data.getViewTab() == tab) {
				if (minLv == -1)
					minLv = data.getShowVipLv();
				else
					minLv = (data.getShowVipLv() < minLv ? data.getShowVipLv()
							: minLv);
			}
		}
		return minLv;
	}

	public boolean canOpenTab(byte tab) {
		int curVipLvl = Account.user.getCurVip().getLevel();
		return curVipLvl >= getMinVipLvByTab(tab);
	}

	public String getTabName(byte tab) {
		switch (tab) {
		case ShopData.TAB1:
			return "道具";
		case ShopData.TAB2:
			return "残章";
		case ShopData.TAB3:
			return "将魂";
		default:
			return "";
		}
	}

	public int getEnterMinVipLvl() {
		int minLv = -1;
		for (Object obj : list) {
			ShopData data = (ShopData) obj;
			if (minLv == -1)
				minLv = data.getShowVipLv();
			else
				minLv = (data.getShowVipLv() < minLv ? data.getShowVipLv()
						: minLv);
		}
		return minLv;
	}

}
