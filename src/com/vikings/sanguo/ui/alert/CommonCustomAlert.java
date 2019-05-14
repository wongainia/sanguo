package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;

import com.vikings.sanguo.R;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class CommonCustomAlert extends CustomConfirmDialog {
	private static final int layout = R.layout.alert_common_custom;

	/**
	 * 
	 * @param title
	 * @param scale
	 * @param touchClose
	 *            与后面的三个监听是互斥的
	 * @param desc
	 * @param str1
	 * @param call1
	 * @param str2
	 * @param call2
	 * @param str3
	 * @param isClose
	 */
	public CommonCustomAlert(String title, int scale, boolean touchClose,
			String desc, String str1, final CallBack call1, String str2,
			final CallBack call2, String str3, boolean isClose) {
		super(title, scale, touchClose);
		ViewUtil.setRichText(content, R.id.desc, desc, true);
		if (!touchClose) {
			if (null != call1) {
				setButton(FIRST_BTN, str1, new OnClickListener() {

					@Override
					public void onClick(View v) {
						dismiss();
						call1.onCall();
					}
				});
			}

			if (null != call2) {
				setButton(SECOND_BTN, str2, new OnClickListener() {

					@Override
					public void onClick(View v) {
						dismiss();
						call2.onCall();
					}
				});
			}

			if (isClose) {
				setButton(THIRD_BTN, str3, closeL);
			}
		}
	}

	public void show() {
		super.show();
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout, tip, false);
	}
}
