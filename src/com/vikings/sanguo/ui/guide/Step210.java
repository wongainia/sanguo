package com.vikings.sanguo.ui.guide;

import android.view.View;
import android.widget.ListView;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.ui.window.CampaignTroopSetWindow;
import com.vikings.sanguo.ui.window.HeroChooseListWindow;
import com.vikings.sanguo.ui.window.PopupUI;

public class Step210 extends BaseStep {

	private View v;
	private View vg;

	@Override
	protected void setUI() {
		PopupUI popupUI = ctr.getCurPopupUI();
		if (popupUI instanceof HeroChooseListWindow) {
			HeroChooseListWindow mHeroChooseListWindow = (HeroChooseListWindow) popupUI;
			ListView lv = mHeroChooseListWindow.getListView();
			if (lv != null && lv.getChildCount() > 0) {
				vg = lv.getChildAt(0);
				if (vg.getVisibility() == View.GONE) {
					vg.setVisibility(View.VISIBLE);
				}
				v = cpGameUI(vg);
				addArrow(v, 7, 0, 0);
			}
		}
	}

	@Override
	protected View getListenerView() {
		return v;
	}

	@Override
	protected void onDestory() {

	}

	@Override
	protected void onDirectQuit() {
		CampaignTroopSetWindow.isGuild = true;
		// 由于vg的单击事件 中有 goBack 方法调用 故必须在 引导界面完全退出 onDirectQuit的方法里调用
		if (vg != null) {
			vg.performClick();
		}
		endGuider(INDEX_STEP206);
	}

	@Override
	protected BaseStep getNextStep() {
		return null;
	}

	@Override
	protected boolean isSpecificWindow() {
		if (null == Config.getController().getCurPopupUI())
			return false;
		return Config.getController().getCurPopupUI() instanceof HeroChooseListWindow;
	}

}
