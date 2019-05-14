package com.vikings.sanguo.ui.guide;

import android.view.View;
import android.widget.ListView;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.ui.window.ArmTrainingListWindow;
import com.vikings.sanguo.ui.window.PopupUI;

public class Step604 extends BaseStep {
	private View vg;
	private View v;

	@Override
	protected void setUI() {

		PopupUI popupUI = ctr.getCurPopupUI();
		if (popupUI instanceof ArmTrainingListWindow) {
			ArmTrainingListWindow mArmTrainingListWindow = (ArmTrainingListWindow) popupUI;
			ListView lv = mArmTrainingListWindow.getListView();
			if (lv != null && lv.getChildCount() > 0) {
				vg = lv.getChildAt(0);
				if (vg != null) {
					v = cpGameUI(vg);
					addArrow(v, 3, 0, 0);
					initPromptView("盾兵");
				}
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
		return new Step605();
	}

	@Override
	protected boolean isSpecificWindow() {
		if (null == Config.getController().getCurPopupUI())
			return false;
		return Config.getController().getCurPopupUI() instanceof ArmTrainingListWindow;
	}

}
