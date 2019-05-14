package com.vikings.sanguo.ui.guide;

import android.view.View;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.ui.window.GodWealthWindow;

public class Step304 extends BaseStep {
	private View v;

	@Override
	protected void setUI() {
		v = cpGameUI(findView(R.id.go_layout));
		addArrow(v, 3, 0, 0);
		initPromptView("拜财神");
	}

	@Override
	protected View getListenerView() {
		return v == null ? window : v;
	}

	@Override
	protected void onDestory() {
		if (findView(R.id.go) != null) {
			findView(R.id.go).performClick();
			// 引导结束
		}
	}


	@Override
	protected BaseStep getNextStep() {
		return null;
	}

	@Override
	protected boolean isSpecificWindow() {
		if (null == Config.getController().getCurPopupUI())
			return false;
		return Config.getController().getCurPopupUI() instanceof GodWealthWindow;
	}

}
