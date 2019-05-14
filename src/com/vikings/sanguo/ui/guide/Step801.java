package com.vikings.sanguo.ui.guide;

import android.view.View;
import android.widget.ListView;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.ui.window.ActTypeWindow;
import com.vikings.sanguo.ui.window.PopupUI;
import com.vikings.sanguo.utils.StringUtil;

public class Step801 extends BaseStep {

	private View v;
	private View vg;

	@Override
	protected void setUI() {
		PopupUI popupUI = ctr.getCurPopupUI();
		if (popupUI instanceof ActTypeWindow) {
			ActTypeWindow mActTypeWindow = (ActTypeWindow) popupUI;
			ListView lv = mActTypeWindow.getListView();
			int position = ActTypeWindow.actTypeBloodPosition();
			if (position != -1 && lv != null && lv.getChildCount() > 0
					&& position < lv.getChildCount()) {
				vg = lv.getChildAt(position);
				if (vg.getVisibility() == View.GONE) {
					vg.setVisibility(View.VISIBLE);
				}
				v = cpGameUI(vg);
				addArrow(v, 7, 0, 0);
				initPromptView("无尽血战");
			}
		}
	}

	@Override
	protected View getListenerView() {
		return v == null ? window : v;
	}

	@Override
	protected void onDestory() {
		if (vg != null && !Account.myLordInfo.hasReward()) {
			vg.performClick();
		}
	}

	public static boolean isFinish() {
		return StringUtil.isFlagOn(Account.user.getTraining(),
				BaseStep.INDEX_STEP801);
	}

	@Override
	protected BaseStep getNextStep() {
		return new Step802();
	}

	@Override
	protected boolean isSpecificWindow() {
		if (null == Config.getController().getCurPopupUI())
			return false;
		return Config.getController().getCurPopupUI() instanceof ActTypeWindow;
	}
}
