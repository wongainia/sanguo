package com.vikings.sanguo.ui.guide;

import android.view.View;
import android.widget.ListView;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.ui.window.EquipmentWearWindow;
import com.vikings.sanguo.ui.window.PopupUI;

public class Step705 extends BaseStep {

	private View v;
	private View vg;

	@Override
	protected void setUI() {
		PopupUI popupUI = ctr.getCurPopupUI();
		if (popupUI instanceof EquipmentWearWindow) {
			EquipmentWearWindow equipmentWearWindow = (EquipmentWearWindow) popupUI;
			ListView lv = equipmentWearWindow.getListView();
			if (lv != null && lv.getChildCount() > 0) {
				vg = lv.getChildAt(0);
				if (vg.getVisibility() == View.GONE) {
					vg.setVisibility(View.VISIBLE);
				}
				v = cpGameUI(vg);
				addArrow(v, 7, 0, 0);
				initPromptView("武器");
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
			endGuider(INDEX_STEP701);
			EquipmentWearWindow.step701_guide = true;
		}
	}

	@Override
	protected void onDirectQuit() {

	}

	@Override
	protected BaseStep getNextStep() {
		return null;
	}

	@Override
	protected boolean isSpecificWindow() {
		if (null == Config.getController().getCurPopupUI())
			return false;
		return Config.getController().getCurPopupUI() instanceof EquipmentWearWindow;
	}
}
