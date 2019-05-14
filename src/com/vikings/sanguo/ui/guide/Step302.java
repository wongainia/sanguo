package com.vikings.sanguo.ui.guide;

import android.view.View;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.EventRewards;
import com.vikings.sanguo.ui.EventRewardsUI;
import com.vikings.sanguo.ui.window.CastleWindow;
import com.vikings.sanguo.ui.window.GodWealthWindow;
import com.vikings.sanguo.ui.window.PopupUI;

public class Step302 extends BaseStep {
	private View view;
	private View v;

	@Override
	protected void setUI() {
		PopupUI popupUI = ctr.getCurPopupUI();
		if (popupUI instanceof CastleWindow) {
			CastleWindow castleWindow = (CastleWindow) popupUI;
			EventRewardsUI eventRewardsUI = castleWindow.getEventRewardUI();
			if (!eventRewardsUI.isShowRewards()) {
				eventRewardsUI.open(EventRewardsUI.TYPE_EVENT);
			}
			if (eventRewardsUI.getGridView() != null
					&& eventRewardsUI.getGridView().getChildCount() != 0) {
				for (int j = 0; j < eventRewardsUI.getGridView()
						.getChildCount(); j++) {
					EventRewards eRewards = (EventRewards) (eventRewardsUI
							.getAdapter()).getItem(j);
					if (eRewards.getId() == 16/* 天降横财 */) {
						v = eventRewardsUI.getGridView().getChildAt(j);
						view = cpGameUI(v);
						addArrow(v, 3, 0, 0);
						break;
					}
				}
			}

		}

	}

	@Override
	protected View getListenerView() {
		return view == null ? window : view;
	}

	@Override
	protected void onDestory() {

		if (v != null) {
			GodWealthWindow.isGuide = true;
			v.performClick();
		}
	}

	@Override
	protected BaseStep getNextStep() {
		return new Step303();
	}

	@Override
	protected boolean isSpecificWindow() {
		if (null == Config.getController().getCurPopupUI())
			return false;
		return Config.getController().getCurPopupUI() instanceof CastleWindow;
	}

}
