package com.vikings.sanguo.ui.guide;

import android.view.View;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.ActInfoClient;
import com.vikings.sanguo.ui.window.CampaignWindow;
import com.vikings.sanguo.ui.window.CastleWindow;

public class Step2071 extends BaseStep {
	private View v;

	@Override
	protected void setUI() {
		if (findView(R.id.campaignBt) != null) {
			v = cpGameUI(findView(R.id.campaignBt),
					(int) (21 * Config.SCALE_FROM_HIGH));
			addArrow(v, 3, 0, 0);
			initPromptView("副本");
		}
	}

	@Override
	protected View getListenerView() {
		return v == null ? window : v;
	}

	@Override
	protected void onDestory() {
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
		return Config.getController().getCurPopupUI() instanceof CastleWindow;
	}
}
