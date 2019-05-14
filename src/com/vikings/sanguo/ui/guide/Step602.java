package com.vikings.sanguo.ui.guide;

import android.view.View;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BuildingProp;
import com.vikings.sanguo.ui.window.ArmTrainingListWindow;
import com.vikings.sanguo.ui.window.CastleWindow;

public class Step602 extends BaseStep {
	private View v;

	@Override
	protected void setUI() {
		v = cpGameUiByBuildingType(BuildingProp.BUILDING_TYPE_ARM_ENROLL);
		initPromptView("募兵所");
	}

	@Override
	protected View getListenerView() {
		return v == null ? window : v;
	}

	@Override
	protected void onDestory() {
		new ArmTrainingListWindow().open();
	}

	@Override
	protected BaseStep getNextStep() {
		return new Step603();
	}

	@Override
	protected boolean isSpecificWindow() {
		if (null == Config.getController().getCurPopupUI())
			return false;
		return Config.getController().getCurPopupUI() instanceof CastleWindow;
	}
}
