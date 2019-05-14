package com.vikings.sanguo.ui.guide;

import android.view.View;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.ui.window.SingleRefreshWindow;

public class Step205 extends BaseStep {

	private View v;

	@Override
	protected void setUI() {
		v = cpGameUI(findView(R.id.refreshBtn));
		addArrow(v, 3, 0, 0);
	}

	@Override
	protected View getListenerView() {
		return v;
	}

	@Override
	protected void onDestory() {
		// 判断钱是否足够
		SingleRefreshWindow srw = (SingleRefreshWindow) ctr.getCurPopupUI();
		// 假招募
		srw.isGuild = true;
		findView(R.id.refreshBtn).performClick();
	}

	@Override
	protected BaseStep getNextStep() {
		return new Step206();
	}

	@Override
	protected boolean isSpecificWindow() {
		if (null == Config.getController().getCurPopupUI())
			return false;
		return Config.getController().getCurPopupUI() instanceof SingleRefreshWindow;
	}

}
