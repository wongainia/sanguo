package com.vikings.sanguo.cache;

import java.util.List;

import com.vikings.sanguo.model.BuildingBuyCost;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.ShowItem;
import com.vikings.sanguo.utils.ListUtil;

public class BuildingBuyCostCache extends LazyLoadArrayCache {

	private static final String FILE_NAME = "building_buy_cost.csv";

	@Override
	public long getSearchKey1(Object obj) {
		return ((BuildingBuyCost) obj).getBuildingId();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return ((BuildingBuyCost) obj).getCostId();
	}

	@Override
	public Object fromString(String line) {
		return BuildingBuyCost.fromString(line);
	}

	@Override
	public String getName() {
		return FILE_NAME;
	}

	public boolean isEnough(int buildingId) {
		ReturnInfoClient ric = new ReturnInfoClient();
		List<BuildingBuyCost> list = CacheMgr.buildingBuyCostCache.search(buildingId);
		for (BuildingBuyCost cost : list) 
			ric.addCfg(cost.getCostType(), cost.getCostId(), cost.getCount());
		
		List<ShowItem> items = ric.checkRequire();
		return ListUtil.isNull(items);
	}
}
