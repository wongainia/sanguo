package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;

import com.vikings.sanguo.R;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class TouchCloseConfirm extends CustomConfirmDialog {

	public TouchCloseConfirm() {
		super(CustomConfirmDialog.DEFAULT, true);
	}

	@Override
	protected View getContent() {
		return controller.inflate(R.layout.touch_close_confirm);
	}

	public void show(String str, String title, OnClickListener l) {
		setTitle(title);
		ViewUtil.setBoldRichText(content.findViewById(R.id.content), str);
		setButton(CustomConfirmDialog.THIRD_BTN, "确        定", l);
		super.show();
		ViewUtil.setGone(tip, R.id.closeDesc);
	}
}
