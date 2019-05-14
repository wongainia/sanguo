package com.vikings.sanguo.ui.guide;

import android.view.View;
import android.widget.ListView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.ui.window.CampaignWindow;
import com.vikings.sanguo.ui.window.PopupUI;

public class Step102 extends BaseStep {

	private View v;
	private View vg;

	@Override
	protected void setUI() {
		PopupUI popupUI = ctr.getCurPopupUI();
		if (popupUI instanceof CampaignWindow) {

			CampaignWindow mCampaignWindow = (CampaignWindow) popupUI;
			ListView lv = mCampaignWindow.getListView();
			if (lv != null && lv.getChildCount() > 0) {
				vg = lv.getChildAt(0);
				v = cpGameUI(vg.findViewById(R.id.fightBtn));
				addArrow(v, 3, 0, 0);
				initPromptView("撸");
			}
		}

	}

	@Override
	protected View getListenerView() {
		return v;
	}

	@Override
	protected void onDestory() {
		vg.findViewById(R.id.fightBtn).performClick();
	}

	@Override
	protected BaseStep getNextStep() {
		return new Step103();
	}

	@Override
	protected boolean isSpecificWindow() {
		if (null == Config.getController().getCurPopupUI())
			return false;
		return Config.getController().getCurPopupUI() instanceof CampaignWindow;
	}
}
