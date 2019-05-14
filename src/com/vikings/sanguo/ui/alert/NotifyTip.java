package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;

import com.vikings.sanguo.R;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class NotifyTip extends CustomConfirmDialog {
	private static final int layout = R.layout.alert_confirm;

	private String desc;
	private CallBack callback;

	public NotifyTip(String buttonStr, String desc, CallBack cb) {
		super("通知", DEFAULT);
		this.desc = desc;
		this.callback = cb;
		if (!StringUtil.isNull(buttonStr))
			setButton(FIRST_BTN, buttonStr, new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (null != callback) {
						dismiss();
						callback.onCall();
					}
				}
			});
		setButton(SECOND_BTN, "关闭", closeL);
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}

	public void show() {
		setValue();
		super.show();
	}

	private void setValue() {
		ViewUtil.setRichText(content, R.id.msg, desc);
		ViewUtil.setGone(content, R.id.msgExt);
	}
}
