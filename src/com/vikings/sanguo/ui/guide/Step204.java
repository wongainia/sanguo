package com.vikings.sanguo.ui.guide;

import android.view.View;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.model.UITextProp;

public class Step204 extends BaseStep {

	@Override
	protected void setUI() {
		addUICenterHeroSpeakTip(CacheMgr.uiTextCache
				.getTxt(UITextProp.GUIDE_DESC_12));
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
		return new Step205();
	}

}
