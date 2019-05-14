package com.vikings.sanguo.ui.guide;

import android.view.View;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.UITextProp;
import com.vikings.sanguo.ui.window.CastleWindow;
import com.vikings.sanguo.utils.StringUtil;

//首次登录游戏的指引
public class Step100 extends BaseStep {

	@Override
	protected void setUI() {
		addUICenterHeroSpeakTip(CacheMgr.uiTextCache
				.getTxt(UITextProp.GUIDE_DESC_01));
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
		return new Step101();
	}

	public static boolean isFinish() {
		return StringUtil.isFlagOn(Account.user.getTraining(),
				BaseStep.INDEX_STEP100);
	}

	@Override
	protected boolean isSpecificWindow() {
		if (null == Config.getController().getCurPopupUI())
			return false;
		return Config.getController().getCurPopupUI() instanceof CastleWindow;
	}

}
