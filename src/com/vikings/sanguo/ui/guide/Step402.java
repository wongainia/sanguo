package com.vikings.sanguo.ui.guide;

import android.view.View;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BuildingProp;
import com.vikings.sanguo.ui.window.CastleWindow;
import com.vikings.sanguo.ui.window.HeroCenterWindow;

public class Step402 extends BaseStep {

	private View v;

	@Override
	protected void setUI() {
		v = cpGameUiByBuildingType(BuildingProp.BUILDING_TYPE_HERO_CENTER);

		initPromptView("英雄殿");
	}

	@Override
	protected View getListenerView() {
		return v;
	}

	@Override
	protected void onDestory() {
		new HeroCenterWindow().open();
	}

	@Override
	protected BaseStep getNextStep() {
		return new Step403();
	}

	@Override
	protected boolean isSpecificWindow() {
		if (null == Config.getController().getCurPopupUI())
			return false;
		return Config.getController().getCurPopupUI() instanceof CastleWindow;
	}
}
