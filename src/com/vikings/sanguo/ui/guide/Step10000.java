package com.vikings.sanguo.ui.guide;

import android.view.View;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.ui.window.CastleWindow;

public class Step10000 extends BaseStep {

	private String prompt;

	public Step10000(String prompt) {
		super();
		this.prompt = prompt;
	}

	@Override
	protected void setUI() {
		addUICenterHeroSpeakTip(prompt);
	}

	@Override
	protected View getListenerView() {
		return window;
	}

	@Override
	protected void onDestory() {
	}

	@Override
	protected BaseStep getNextStep() {
		return null;
	}

	@Override
	protected boolean isSpecificWindow() {
		if (null == Config.getController().getCurPopupUI())
			return false;
		return Config.getController().getCurPopupUI() instanceof CastleWindow;
	}

}
