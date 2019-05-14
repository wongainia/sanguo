package com.vikings.sanguo.ui.guide;

import android.view.View;

public class Step7052 extends BaseStep {

	private View v;

	@Override
	protected void setUI() {
		View view = ctr.getBackKey();
		if (view == null) {
			return;
		}
		if (view.getVisibility() == View.GONE) {
			view.setVisibility(View.VISIBLE);
		}
		v = cpGameUI(ctr.getBackKey());
		addArrow(v, 7, 0, 0);
		initPromptView("回退");

	}

	@Override
	protected View getListenerView() {
		return v == null ? window : v;
	}

	@Override
	protected void onDestory() {

	}

	@Override
	protected void onDirectQuit() {
		ctr.goBack();
		new Step7053().run();
	}

	@Override
	protected BaseStep getNextStep() {
		return null;
	}

	// @Override
	// protected boolean isSpecificWindow() {
	// if (null == Config.getController().getCurPopupUI())
	// return false;
	// return Config.getController().getCurPopupUI() instanceof CastleWindow;
	// }
}
