package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;

import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BuildingFiefBuyInvoker;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.BuildingEffect;
import com.vikings.sanguo.model.BuildingInfoClient;
import com.vikings.sanguo.model.BuildingProp;
import com.vikings.sanguo.model.BuildingStatus;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.utils.StringUtil;

public class BuildingFiefBuyTip extends BuildingBuyTip {

	private BriefFiefInfoClient fief;

	public BuildingFiefBuyTip(BuildingProp buildingProp) {
		super(buildingProp);
	}

	public void show(BuildingProp buildingProp, BriefFiefInfoClient fief) {
		this.fief = fief;
		bic = fief.getBuilding();
		if (null != bic)
			setTitle("升级" + bic.getProp().getBuildingName());
		else
			setTitle("购买" + buildingProp.getBuildingName());
		super.show();
	}

	@Override
	protected BuildingInfoClient getBic() {
		return fief.getBuilding();
	}

	@Override
	protected OnClickListener getListener(final int type) {

		return new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checkCondition(type))
					new BuildingFiefBuyImplInvoker(type, buildingProp,
							fief.getId()).start();
			}
		};
	}

	private class BuildingFiefBuyImplInvoker extends BuildingFiefBuyInvoker {

		public BuildingFiefBuyImplInvoker(int type, BuildingProp prop,
				long fiefId) {
			super(type, prop, fiefId);
		}

		@Override
		protected void onOK() {
			super.onOK();
			fief.update(Account.richFiefCache.getInfo(fiefId).brief());
			if (prop.getNextLevel() != null)
				buildingProp = prop.getNextLevel();
			else
				buildingProp = prop;
			setValue();
		}

	}

	@Override
	protected String getBuildingDesc(BuildingProp buildingProp) {
		String speed = getSpeed();
		if (buildingProp.getType() == fief.getProp().getBuildingType()
				&& !StringUtil.isNull(speed)) {
			return "每小时产出 " + speed + "（最大不超过资源点产能上限）<br>（需手动前往收获）";
		} else {
			return buildingProp.getDesc();
		}
	}

	private String getSpeed() {

		try {
			BuildingStatus status = (BuildingStatus) CacheMgr.buildingStatusCache
					.get(buildingProp.getId());
			BuildingInfoClient bic = Account.manorInfoClient
					.getBuilding(BuildingProp.BUILDING_TYPE_SCIENCE);
			if (null != bic && null != bic.getProp()) {
				BuildingProp prop = bic.getProp();
				BuildingEffect effect = (BuildingEffect) CacheMgr.buildingEffectCache
						.search(prop.getId(), (status.getStatusId() + 51));
				if (null != effect)
					return (int) (effect.getValue() * status.getValue() / 100f)
							+ " "
							+ ReturnInfoClient.getAttrTypeName(status
									.getStatusId());
			}
		} catch (GameException e) {

		}
		return "";

	}

}
