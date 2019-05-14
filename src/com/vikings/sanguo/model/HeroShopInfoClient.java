package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.HeroShopInfo;
import com.vikings.sanguo.protos.HeroShopItemInfo;

public class HeroShopInfoClient {
	private List<HeroShopItemInfoClient> infos = new ArrayList<HeroShopItemInfoClient>();

	public List<HeroShopItemInfoClient> getInfos() {
		return infos;
	}

	public void setInfos(List<HeroShopItemInfoClient> itemInfos) {
		this.infos.clear();
		this.infos.addAll(itemInfos);
	}

	public void setEmpty() {
		this.infos.clear();
	}

	public static HeroShopInfoClient convert(HeroShopInfo info)
			throws GameException {
		HeroShopInfoClient hsic = new HeroShopInfoClient();
		List<HeroShopItemInfoClient> itemInfoClients = new ArrayList<HeroShopItemInfoClient>();
		if (null != info) {
			List<HeroShopItemInfo> list = info.getInfosList();
			if (null != list && !list.isEmpty()) {
				for (HeroShopItemInfo itemInfo : list) {
					itemInfoClients.add(HeroShopItemInfoClient
							.convert(itemInfo));
				}
			}
		}
		hsic.setInfos(itemInfoClients);
		return hsic;
	}
}
