package com.vikings.sanguo.ui.guide;

import android.view.View;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.ui.window.ArmTrainingWindow;

public class Step605 extends BaseStep {
	private View view;
	private View v;

	@Override
	protected void setUI() {
		view = findView(R.id.goldFrameBtn);
		v = cpGameUI(view);
		addArrow(v, 3, 0, 0);
		initPromptView("招募");
	}

	@Override
	protected View getListenerView() {
		return v == null ? window : v;
	}

	@Override
	protected void onDestory() {
		if (view != null) {
			view.performClick();
		}
		endGuider(INDEX_STEP601);
	}

	@Override
	protected BaseStep getNextStep() {
		return null;
	}

	@Override
	protected boolean isSpecificWindow() {
		if (null == Config.getController().getCurPopupUI())
			return false;
		return Config.getController().getCurPopupUI() instanceof ArmTrainingWindow;
	}
}
