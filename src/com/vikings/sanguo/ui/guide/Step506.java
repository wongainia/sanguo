package com.vikings.sanguo.ui.guide;

import android.view.View;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.ui.window.TroopMoveDetailWindow;

public class Step506 extends BaseStep {
	private View v;

	@Override
	protected void setUI() {
		v = cpGameUI(findView(R.id.belowBtn));
		((TroopMoveDetailWindow) Config.getController().getCurPopupUI())
				.setMainInfo();
		addArrow(v, 3, 0, 0);
		initPromptView("占领");
	}

	@Override
	protected View getListenerView() {
		return v;
	}

	@Override
	protected void onDestory() {
		((TroopMoveDetailWindow) Config.getController().getCurPopupUI())
				.guideOccupy();
	}

	@Override
	protected BaseStep getNextStep() {
		return null;
	}

	@Override
	protected boolean isSpecificWindow() {
		if (null == Config.getController().getCurPopupUI())
			return false;
		return Config.getController().getCurPopupUI() instanceof TroopMoveDetailWindow;
	}
}
