package com.vikings.sanguo.ui.guide;

import android.view.View;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.ActInfoClient;
import com.vikings.sanguo.model.UITextProp;
import com.vikings.sanguo.ui.window.CampaignWindow;
import com.vikings.sanguo.ui.window.SingleRefreshWindow;

public class Step207 extends BaseStep {

	@Override
	protected void setUI() {
		addUIHeroSpeakTip(
				CacheMgr.uiTextCache.getTxt(UITextProp.GUIDE_DESC_13), 100);
	}

	@Override
	protected View getListenerView() {
		return window;
	}

	@Override
	protected void onDestory() {
		ctr.showCastle(Account.user.getId());
		new CampaignWindow().openGuard(Account.actInfoCache
				.getAct(ActInfoClient.GUILD_FIRST_ACT_ID));
	}

	@Override
	protected BaseStep getNextStep() {
		return new Step208();
	}

	@Override
	protected boolean isSpecificWindow() {
		if (null == Config.getController().getCurPopupUI())
			return false;
		return Config.getController().getCurPopupUI() instanceof SingleRefreshWindow;
	}

}
