package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class ScrollDescTip extends CustomConfirmDialog {
	private static final int layout = R.layout.alert_scroll_desc;
	private TextView view;
	private String desc;

	public ScrollDescTip(String title, String desc) {
		super(title, DEFAULT);
		this.desc = desc;
		view = (TextView) content.findViewById(R.id.desc);
		setButton(FIRST_BTN, "关闭", closeL);
	}

	public void show() {
		super.show();
		setValue();
	}

	private void setValue() {
		ViewUtil.setRichText(view, desc);
	}

	// 显示的列表布局
	@Override
	protected View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}

}
