package com.vikings.sanguo.ui.guide;

import android.view.View;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.ui.window.SingleRefreshWindow;

public class Step206 extends BaseStep {

	@Override
	protected void setUI() {
	}

	@Override
	protected void preSetWindowBackground() {
		window.setBackgroundColor(Config.getController().getResources()
				.getColor(android.R.color.transparent));
	}

	@Override
	protected View getListenerView() {
		return window;
	}

	@Override
	protected void onDestory() {
	}

	@Override
	protected BaseStep getNextStep() {
		return new Step207();
	}

	@Override
	protected boolean isSpecificWindow() {
		if (null == Config.getController().getCurPopupUI())
			return false;
		return Config.getController().getCurPopupUI() instanceof SingleRefreshWindow;
	}

}
