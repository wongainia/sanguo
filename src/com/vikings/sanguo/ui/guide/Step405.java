package com.vikings.sanguo.ui.guide;

import android.view.View;
import android.widget.Button;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.ui.window.HeroEvolveWindow;

public class Step405 extends BaseStep {
	private Button view;
	private View v;

	@Override
	protected void setUI() {
		view = (Button) findView(R.id.evolveBtn);
		if (view != null) {
			HeroEvolveWindow.isGuide = true;
			view.setText("开始进化");
			v = cpGameUI(view);
			addArrow(v, 3, 0, 0);
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
		endGuider(INDEX_STEP401);
		Account.readLog.STEP1001 = 1;
		Account.readLog.save();
	}

	@Override
	protected BaseStep getNextStep() {
		return null;
	}

	@Override
	protected boolean isSpecificWindow() {
		if (null == Config.getController().getCurPopupUI())
			return false;
		return Config.getController().getCurPopupUI() instanceof HeroEvolveWindow;
	}
}
