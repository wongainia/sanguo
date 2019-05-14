package com.vikings.sanguo.ui.guide;

import android.view.View;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.ui.window.BloodWindow;

public class Step803 extends BaseStep {

	private View v;
	private View bloodBtn;

	@Override
	protected void setUI() {
		bloodBtn = (View) findView(R.id.bloodBtn);
		if (bloodBtn == null) {
			return;
		}
		if (bloodBtn.getVisibility() == View.GONE) {
			bloodBtn.setVisibility(View.VISIBLE);
		}
		v = cpGameUI(findView(R.id.bloodBtn));
		addArrow(v, 3, 0, 0);
		initPromptView("开启血战");

	}

	@Override
	protected View getListenerView() {
		return v == null ? window : v;
	}

	@Override
	protected void onDestory() {
		if (bloodBtn != null)
			bloodBtn.performClick();
	}

	@Override
	protected BaseStep getNextStep() {
		return new Step804();
	}

	@Override
	protected boolean isSpecificWindow() {
		if (null == Config.getController().getCurPopupUI())
			return false;
		return Config.getController().getCurPopupUI() instanceof BloodWindow;
	}

}
