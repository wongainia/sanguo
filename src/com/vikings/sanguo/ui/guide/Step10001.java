package com.vikings.sanguo.ui.guide;

import android.view.View;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.UITextProp;
import com.vikings.sanguo.ui.window.CampaignTroopSetWindow;
import com.vikings.sanguo.utils.StringUtil;

//战役副本 1-6关 引导
public class Step10001 extends BaseStep {

	@Override
	protected void setUI() {
		addUICenterHeroSpeakTip(CacheMgr.uiTextCache
				.getTxt(UITextProp.GUIDE_DESC_2111));

	}

	@Override
	protected View getListenerView() {
		return window;
	}

	@Override
	protected void onDestory() {
	}

	public static boolean checkCondition(int actId) {
		if (actId == ACT_CAMPAINGN_1_5 && !isFinish()) {
			return true;
		}
		return false;
	}

	@Override
	protected BaseStep getNextStep() {
		return new Step10002();
	}

	public static boolean isFinish() {
		return StringUtil.isFlagOn(Account.user.getTraining(),
				BaseStep.INDEX_STEP10001);
	}

	@Override
	protected boolean isSpecificWindow() {
		if (null == Config.getController().getCurPopupUI())
			return false;
		return Config.getController().getCurPopupUI() instanceof CampaignTroopSetWindow;
	}
}
