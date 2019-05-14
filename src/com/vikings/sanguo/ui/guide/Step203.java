package com.vikings.sanguo.ui.guide;

import android.view.View;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.ui.window.BarWindow;

public class Step203 extends BaseStep {

	private View v;

	@Override
	protected void setUI() {
		v = cpGameUI(findView(R.id.singleBtn));
		addArrow(v, 3, 0, 0);
		initPromptView("盲选");
	}

	@Override
	protected View getListenerView() {
		return v;
	}

	@Override
	protected void onDestory() {
		if (findView(R.id.singleBtn) != null) {
			findView(R.id.singleBtn).performClick();
		}
	}

	@Override
	protected BaseStep getNextStep() {
		return new Step204();
	}

	@Override
	protected boolean isSpecificWindow() {
		if (null == Config.getController().getCurPopupUI())
			return false;
		return Config.getController().getCurPopupUI() instanceof BarWindow;
	}

}
