package com.vikings.sanguo.ui.alert;

import android.view.View;

import com.vikings.sanguo.R;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class ActClearLoseTip extends CustomConfirmDialog {

	public ActClearLoseTip() {
		super("扫荡副本失败");
		setButton(FIRST_BTN, "确    定", closeL);
	}

	@Override
	protected View getContent() {
		return controller.inflate(R.layout.alert_act_clear_lose, contentLayout,
				false);
	}
}
