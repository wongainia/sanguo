package com.vikings.sanguo.ui.guide;

import android.view.View;
import android.widget.ListView;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.ui.window.ArenaWindow;
import com.vikings.sanguo.ui.window.PopupUI;

public class Step902 extends BaseStep {

	private View v;
	private View vg;

	@Override
	protected void setUI() {
		PopupUI popupUI = ctr.getCurPopupUI();
		if (popupUI instanceof ArenaWindow) {
			ArenaWindow mArenaWindow = (ArenaWindow) popupUI;
			ListView lv = mArenaWindow.getListView();
			if (lv != null && lv.getChildCount() > 0) {
				vg = lv.getChildAt(lv.getChildCount() - 1);
				if (vg.getVisibility() == View.GONE) {
					vg.setVisibility(View.VISIBLE);
				}
				v = cpGameUI(vg);
				addArrow(v, 3, 0, 0);
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
		endGuider(INDEX_STEP901);
	}

	@Override
	protected BaseStep getNextStep() {
		return null;
	}

	@Override
	protected boolean isSpecificWindow() {
		if (null == Config.getController().getCurPopupUI())
			return false;
		return Config.getController().getCurPopupUI() instanceof ArenaWindow;
	}
}
