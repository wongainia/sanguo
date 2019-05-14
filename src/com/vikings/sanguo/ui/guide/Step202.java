package com.vikings.sanguo.ui.guide;

import android.view.View;

import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BuildingProp;
import com.vikings.sanguo.ui.window.BarWindow;
import com.vikings.sanguo.ui.window.CastleWindow;
import com.vikings.sanguo.utils.StringUtil;

public class Step202 extends BaseStep {
	private View v;

	@Override
	protected void setUI() {
		v = cpGameUiByBuildingType(BuildingProp.BUILDING_TYPE_BAR);
		initPromptView("红楼");
	}

	@Override
	protected View getListenerView() {
		return v;
	}

	public static boolean isFinish() {
		return StringUtil.isFlagOn(Account.user.getTraining(),
				BaseStep.INDEX_STEP201);
	}

	@Override
	protected void onDestory() {
		new BarWindow().open();
	}

	@Override
	protected BaseStep getNextStep() {
		return new Step2021();
	}

	@Override
	protected boolean isSpecificWindow() {
		if (null == Config.getController().getCurPopupUI())
			return false;
		return Config.getController().getCurPopupUI() instanceof CastleWindow;
	}

}
