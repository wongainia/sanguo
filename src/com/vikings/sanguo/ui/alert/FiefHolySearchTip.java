package com.vikings.sanguo.ui.alert;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.model.HolyCategory;
import com.vikings.sanguo.ui.PressedZoomButton;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class FiefHolySearchTip extends CustomConfirmDialog {

	/**
	 * @param title
	 * @param sacred
	 *            是否圣都
	 */
	public FiefHolySearchTip(String title, boolean sacred) {
		super(title, DEFAULT);
		setCloseBtn();

		List<HolyCategory> ls = CacheMgr.holyCategoryCache.getAll();
		for (int i = 0; i < ls.size(); i++) {
			HolyCategory hc = ls.get(i);
			if (sacred) {
				if (hc.getHolyType() == HolyCategory.SHENGDU
						|| hc.getHolyType() == HolyCategory.SHENJI) {
					addView(hc);
				}
			} else {
				if (hc.getHolyType() != HolyCategory.SHENGDU
						&& hc.getHolyType() != HolyCategory.SHENJI) {
					addView(hc);
				}
			}

		}
	}

	protected void addView(final HolyCategory hc) {
		PressedZoomButton btn = (PressedZoomButton) controller.inflate(
				R.layout.holy_btn, tip, false);
		ViewUtil.setText(btn, hc.getBtnTxt());
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				new HolyFiefSearchTip(hc).show();
			}
		});
		((ViewGroup) content).addView(btn);
	}

	@Override
	protected View getContent() {
		return controller.inflate(R.layout.alert_holy_category, tip, false);
	}
}
