package com.vikings.sanguo.ui.guide;

import android.view.View;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.UITextProp;
import com.vikings.sanguo.ui.window.ArmTrainingListWindow;

public class Step603 extends BaseStep {

	@Override
	protected void setUI() {
		addUICenterHeroSpeakTip(CacheMgr.uiTextCache
				.getTxt(UITextProp.GUIDE_DESC_62));
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
		return new Step604();
	}

	@Override
	protected boolean isSpecificWindow() {
		if (null == Config.getController().getCurPopupUI())
			return false;
		return Config.getController().getCurPopupUI() instanceof ArmTrainingListWindow;
	}
}
