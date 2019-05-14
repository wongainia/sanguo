package com.vikings.sanguo.ui.guide;

import android.view.View;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.UITextProp;
import com.vikings.sanguo.ui.alert.VipFailTip;
import com.vikings.sanguo.ui.window.BattleResultWindow;
import com.vikings.sanguo.utils.StringUtil;

//战役副本 1-4关 引导
public class Step15001 extends BaseStep {

	@Override
	protected void setUI() {
		addUICenterHeroSpeakTip(CacheMgr.uiTextCache
				.getTxt(UITextProp.GUIDE_DESC_2501));
	}

	@Override
	protected View getListenerView() {
		return window;
	}

	@Override
	protected void onDestory() {
		new VipFailTip(2).show();
	}

	@Override
	protected BaseStep getNextStep() {
		return null;
	}

	public static boolean isFinish() {
		return StringUtil.isFlagOn(Account.user.getTraining(),
				BaseStep.INDEX_STEP15001);
	}

	@Override
	protected boolean isSpecificWindow() {
		if (null == Config.getController().getCurPopupUI())
			return false;
		return Config.getController().getCurPopupUI() instanceof BattleResultWindow;
	}
}
