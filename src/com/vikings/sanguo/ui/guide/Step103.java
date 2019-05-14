package com.vikings.sanguo.ui.guide;

import android.view.View;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.ui.window.CampaignTroopSetWindow;

public class Step103 extends BaseStep {

	private View v;

	@Override
	protected void setUI() {
		v = cpGameUI(findView(R.id.belowBtn));
		((CampaignTroopSetWindow) Config.getController().getCurPopupUI())
				.setMainInfo();
		addArrow(v, 3, 0, 0);
		initPromptView("出战");

	}

	@Override
	protected View getListenerView() {
		return v;
	}

	@Override
	protected void onDestory() {
		findView(R.id.belowBtn).performClick();
		endGuider(INDEX_STEP100);
	}

	@Override
	protected BaseStep getNextStep() {
		return null;
	}

	@Override
	protected boolean isSpecificWindow() {
		if (null == Config.getController().getCurPopupUI())
			return false;
		return Config.getController().getCurPopupUI() instanceof CampaignTroopSetWindow;
	}
}
