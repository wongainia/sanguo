package com.vikings.sanguo.ui.guide;

import android.view.View;
import android.widget.ListView;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.ui.window.HeroEvolveListWindow;
import com.vikings.sanguo.ui.window.PopupUI;

public class Step404 extends BaseStep {
	private View view;
	private View v;

	@Override
	protected void setUI() {
		PopupUI popupUI = ctr.getCurPopupUI();
		if (popupUI instanceof HeroEvolveListWindow) {
			HeroEvolveListWindow mHeroCenterWindow = (HeroEvolveListWindow) popupUI;
			ListView lv = mHeroCenterWindow.getListView();
			if (lv != null && lv.getChildCount() > 0) {
				view = lv.getChildAt(0);
				v = cpGameUI(view);
				addArrow(v, 7, 0, 0);
				initPromptView("将领");
			}
		}
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
	}

	@Override
	protected BaseStep getNextStep() {
		return new Step405();
	}

	@Override
	protected boolean isSpecificWindow() {
		if (null == Config.getController().getCurPopupUI())
			return false;
		return Config.getController().getCurPopupUI() instanceof HeroEvolveListWindow;
	}
}
