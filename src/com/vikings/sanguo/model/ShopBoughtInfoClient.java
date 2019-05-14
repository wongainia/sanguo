package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.ShopBoughtInfo;

public class ShopBoughtInfoClient {
	List<ShopItemInfoClient> siics;

	public List<ShopItemInfoClient> getSiics() {
		return siics == null ? new ArrayList<ShopItemInfoClient>() : siics;
	}

	public void setSiics(List<ShopItemInfoClient> siics) {
		this.siics = siics;
	}

	public ShopBoughtInfoClient convert(ShopBoughtInfo info)
			throws GameException {
		if (null == info)
			return null;
		ShopBoughtInfoClient sbic = new ShopBoughtInfoClient();
		if (info.hasInfos()) {
			sbic.setSiics(ShopItemInfoClient.convert2List(info.getInfosList()));
		}
		return sbic;
	}
}
