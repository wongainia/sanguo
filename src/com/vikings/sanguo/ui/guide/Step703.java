package com.vikings.sanguo.ui.guide;

import android.view.View;
import android.widget.ListView;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.ui.window.PopupUI;
import com.vikings.sanguo.ui.window.ReviewArmInManorListWindow;

public class Step703 extends BaseStep {

	private View v;
	private View vg;

	@Override
	protected void setUI() {
		PopupUI popupUI = ctr.getCurPopupUI();
		if (popupUI instanceof ReviewArmInManorListWindow) {
			ReviewArmInManorListWindow mReviewArmInManorListWindow = (ReviewArmInManorListWindow) popupUI;
			ListView lv = mReviewArmInManorListWindow.getListView();
			if (lv != null && lv.getChildCount() > 0) {
				vg = lv.getChildAt(0);
				if (vg.getVisibility() == View.GONE) {
					vg.setVisibility(View.VISIBLE);
				}
				v = cpGameUI(vg);
				addArrow(v, 7, 0, 0);
				initPromptView("将领");
			}
		}
	}

	@Override
	protected View getListenerView() {
		return v == null ? window : v;
	}

	@Override
	protected void onDestory() {
		if (vg != null) {
			vg.performClick();
		}
	}

	@Override
	protected BaseStep getNextStep() {
		return new Step704();
	}

	@Override
	protected boolean isSpecificWindow() {
		if (null == Config.getController().getCurPopupUI())
			return false;
		return Config.getController().getCurPopupUI() instanceof ReviewArmInManorListWindow;
	}
}
