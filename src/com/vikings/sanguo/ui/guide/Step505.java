package com.vikings.sanguo.ui.guide;

import com.vikings.sanguo.ui.window.TroopMoveDetailWindow;

import android.view.View;

public class Step505 extends BaseStep {

	private View v;
	private View expeditionBtn;

	public Step505(View expeditionBtn) {
		super();
		this.expeditionBtn = expeditionBtn;
	}

	@Override
	protected void setUI() {
		v = cpGameUI(expeditionBtn);
		addArrow(v, 7, 0, 0);
		initPromptView("出兵征战");
	}

	@Override
	protected View getListenerView() {
		return v;
	}

	@Override
	protected void onDestory() {
		TroopMoveDetailWindow.isGuide = true;
		expeditionBtn.performClick();
	}

	@Override
	protected BaseStep getNextStep() {
		return new Step506();
	}

}
