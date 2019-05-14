package com.vikings.sanguo.ui.guide;

import android.view.View;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.ui.window.CampaignTroopSetWindow;
import com.vikings.sanguo.utils.StringUtil;

public class Step211 extends BaseStep {

	private View v;

	@Override
	protected void setUI() {
		((CampaignTroopSetWindow) Config.getController().getCurPopupUI())
				.setMainSpecificInfo();
		v = cpGameUI(findView(R.id.belowBtn));
		addArrow(v, 3, 0, 0);
		initPromptView("出战");
	}

	@Override
	protected View getListenerView() {
		return v;
	}

	@Override
	protected void onDestory() {
		if (findView(R.id.belowBtn) != null) {
			findView(R.id.belowBtn).performClick();
			endGuider(INDEX_STEP206);
		}

	}

	public static boolean isFinish() {
		return StringUtil.isFlagOn(Account.user.getTraining(),
				BaseStep.INDEX_STEP206);
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
