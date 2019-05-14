package com.vikings.sanguo.ui.guide;

import android.view.View;

import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;

public class Step300 extends BaseStep {

	@Override
	protected void setUI() {
	}

	@Override
	protected void preSetWindowBackground() {
		window.setBackgroundColor(Config.getController().getResources()
				.getColor(android.R.color.transparent));
	}

	@Override
	protected View getListenerView() {
		return window;
	}

	@Override
	protected void onDestory() {
		ctr.showCastle(Account.user.getId());
	}

	@Override
	protected BaseStep getNextStep() {
		return new Step301();
	}

}
