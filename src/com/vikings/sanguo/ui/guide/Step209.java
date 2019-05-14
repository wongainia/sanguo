package com.vikings.sanguo.ui.guide;

import android.view.View;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.window.CampaignTroopSetWindow;
import com.vikings.sanguo.ui.window.HeroChooseListWindow;

public class Step209 extends BaseStep {

	private View v;

	@Override
	protected void setUI() {
		v = cpGameUI(findView(R.id.hero1));
		addArrow(v, 3, 0, 0);
	}

	@Override
	protected View getListenerView() {
		return v;
	}

	@Override
	protected void onDestory() {
		HeroChooseListWindow.isGuide = true;
		HeroChooseListWindow.guideCallBack = new Step210Callback();
		if (findView(R.id.hero1) != null) {
			findView(R.id.hero1).performClick();
		}
	}

	@Override
	protected BaseStep getNextStep() {
		// 留给了Step210Callback 处理 Step210()
		return null;
	}

	@Override
	protected boolean isSpecificWindow() {
		if (null == Config.getController().getCurPopupUI())
			return false;
		return Config.getController().getCurPopupUI() instanceof CampaignTroopSetWindow;
	}

	public class Step210Callback implements CallBack {

		@Override
		public void onCall() {
			new Step210().run();
		}

	}
}
