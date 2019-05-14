package com.vikings.sanguo.ui.guide;

import android.view.View;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.ui.window.CastleWindow;

public class Step502 extends BaseStep {

	private View v;

	@Override
	protected void setUI() {
		v = cpGameUI(findView(R.id.worldBt),
				(int) (21 * Config.SCALE_FROM_HIGH));
		addArrow(v, 3, 0, 0);
		initPromptView("世界");

	}

	@Override
	protected View getListenerView() {
		return v;
	}

	@Override
	protected void onDestory() {
		findView(R.id.worldBt).performClick();
		endGuider(INDEX_STEP501);
	}

	@Override
	protected BaseStep getNextStep() {
		return null;
	}

	@Override
	protected boolean isSpecificWindow() {
		if (null == Config.getController().getCurPopupUI())
			return false;
		return Config.getController().getCurPopupUI() instanceof CastleWindow;
	}
}
