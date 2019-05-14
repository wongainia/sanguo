package com.vikings.sanguo.ui.guide;

import android.view.View;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.ui.window.CastleWindow;

public class Step702 extends BaseStep {

	private View v;
	private View troopBt;

	@Override
	protected void setUI() {
		troopBt = findView(R.id.troopBt);
		if (troopBt == null) {
			return;
		}
		if (troopBt.getVisibility() == View.GONE) {
			troopBt.setVisibility(View.VISIBLE);
		}
		v = cpGameUI(findView(R.id.troopBt),
				(int) (21 * Config.SCALE_FROM_HIGH));
		addArrow(v, 3, 0, 0);
		initPromptView("将领");

	}

	@Override
	protected View getListenerView() {
		return v == null ? window : v;
	}

	@Override
	protected void onDestory() {
		if (troopBt != null)
			troopBt.performClick();
	}

	@Override
	protected BaseStep getNextStep() {
		return new Step703();
	}

	public static boolean checkCondition() {
		if (!Account.allHeroHasFullEquipment() && Account.readLog.Step701 == 1) {
			return true;
		}
		return false;
	}

	@Override
	protected boolean isSpecificWindow() {
		if (null == Config.getController().getCurPopupUI())
			return false;
		return Config.getController().getCurPopupUI() instanceof CastleWindow;
	}
}
