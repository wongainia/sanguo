package com.vikings.sanguo.ui.guide;

import android.view.View;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.ui.window.CastleWindow;

public class Step7053 extends BaseStep {

	private View v;

	@Override
	protected void setUI() {
		View view = findView(R.id.campaignBt);
		if (view == null) {
			return;
		}
		if (view.getVisibility() == View.GONE) {
			view.setVisibility(View.VISIBLE);
		}
		v = cpGameUI(findView(R.id.campaignBt),
				(int) (21 * Config.SCALE_FROM_HIGH));
		addArrow(v, 3, 0, 0);
		initPromptView("副本");

	}

	@Override
	protected View getListenerView() {
		return v == null ? window : v;
	}

	@Override
	protected void onDestory() {
		// new CampaignWindow().openGuard(Account.actInfoCache
		// .getAct(ActInfoClient.GUILD_FIRST_ACT_ID));

		if (findView(R.id.campaignBt) != null) {
			findView(R.id.campaignBt).performClick();
		}
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
