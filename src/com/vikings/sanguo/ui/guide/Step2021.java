package com.vikings.sanguo.ui.guide;

import android.view.View;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.model.UITextProp;

public class Step2021 extends BaseStep {

	@Override
	protected void setUI() {
		addUICenterHeroSpeakTip(CacheMgr.uiTextCache
				.getTxt(UITextProp.GUIDE_DESC_11));
	}

	@Override
	protected View getListenerView() {
		return window;
	}

	@Override
	protected void onDestory() {
		endGuider(INDEX_STEP201);
	}

	@Override
	protected BaseStep getNextStep() {
		return null;
	}

}
