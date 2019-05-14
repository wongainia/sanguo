package com.vikings.sanguo.ui.guide;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.window.HeroCenterWindow;
import com.vikings.sanguo.ui.window.HeroEvolveListWindow;
import com.vikings.sanguo.ui.window.PopupUI;

import android.view.View;
import android.widget.ListView;

public class Step403 extends BaseStep {
	private View view;
	private View v;

	@Override
	protected void setUI() {
		PopupUI popupUI = ctr.getCurPopupUI();
		if (popupUI instanceof HeroCenterWindow) {
			HeroCenterWindow mHeroCenterWindow = (HeroCenterWindow) popupUI;
			ListView lv = mHeroCenterWindow.getListView();
			if (lv != null && lv.getChildCount() > 2) {
				view = lv.getChildAt(1);
				v = cpGameUI(view);
				addArrow(v, 3, 0, 0);
				initPromptView("进化");
			}
		}
	}

	@Override
	protected View getListenerView() {
		return v == null ? window : v;
	}

	@Override
	protected void onDestory() {
		HeroEvolveListWindow.mStep404CallBack = new Step404Callback();
		if (view != null) {
			view.performClick();
		}
	}

	@Override
	protected BaseStep getNextStep() {
		// 留给了Step404Callback 处理 Step404()
		return null;
	}

	@Override
	protected boolean isSpecificWindow() {
		if (null == Config.getController().getCurPopupUI())
			return false;
		return Config.getController().getCurPopupUI() instanceof HeroCenterWindow;
	}

	public class Step404Callback implements CallBack {

		@Override
		public void onCall() {
			new Step404().run();
		}
	}
}
