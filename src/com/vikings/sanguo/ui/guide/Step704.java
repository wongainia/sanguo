package com.vikings.sanguo.ui.guide;

import android.view.View;
import android.view.ViewGroup;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.ui.window.OwnHeroDetailWindow;

public class Step704 extends BaseStep {

	private View v;
	// 只处理添加武器
	private ViewGroup equipmentLayout1;

	@Override
	protected void setUI() {
		equipmentLayout1 = (ViewGroup) findView(R.id.equipmentLayout1);
		if (equipmentLayout1 == null) {
			return;
		}
		if (equipmentLayout1.getVisibility() == View.GONE) {
			equipmentLayout1.setVisibility(View.VISIBLE);
		}
		v = cpGameUI(findView(R.id.equipmentLayout1));
		addArrow(v, 7, 0, 0);
		initPromptView("添加武器");

	}

	@Override
	protected View getListenerView() {
		return v == null ? window : v;
	}

	@Override
	protected void onDestory() {
		if (equipmentLayout1 != null)
			equipmentLayout1.performClick();
	}

	@Override
	protected BaseStep getNextStep() {
		return new Step705();
	}

	@Override
	protected boolean isSpecificWindow() {
		if (null == Config.getController().getCurPopupUI())
			return false;
		return Config.getController().getCurPopupUI() instanceof OwnHeroDetailWindow;
	}

}
