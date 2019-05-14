package com.vikings.sanguo.cache;

import java.util.List;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.model.BuildingProp;
import com.vikings.sanguo.model.ManorDraftResource;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.ShowItem;
import com.vikings.sanguo.model.TroopProp;

public class ManorDraftResourceCache extends ArrayFileCache {
	private static final String FILE_NAME = "manor_draft_resource.csv";

	@Override
	public long getSearchKey1(Object obj) {
		ManorDraftResource mdr = (ManorDraftResource) obj;
		return buildKey(mdr.getArmId(), mdr.getBuildingId());
	}

	@Override
	public long getSearchKey2(Object obj) {
		ManorDraftResource mdr = (ManorDraftResource) obj;
		return buildKey(mdr.getResourceType(), mdr.getValue());
	}

	@Override
	public Object fromString(String line) {
		return ManorDraftResource.fromString(line);
	}

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@SuppressWarnings("unchecked")
	public List<ManorDraftResource> searchResourceList(int armId, int buildingId) {
		return search(buildKey(armId, buildingId));
	}

	public List<ShowItem> checkResourceEnough(TroopProp tp, BuildingProp bp,
			int count) {
		List<ManorDraftResource> resources = searchResourceList(tp.getId(),
				bp.getId());

		ReturnInfoClient ric = new ReturnInfoClient();
		for (ManorDraftResource resource : resources) {
			ric.addCfg(
					resource.getResourceType(),
					resource.getValue(),
					(int) (1f * resource.getAmount() * count / Constants.PER_TEN_THOUSAND));
		}
		return ric.checkRequire();
	}
}
