package com.vikings.sanguo.ui.alert;

import android.view.View;

import com.vikings.sanguo.R;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class CommonAlert extends Alert {
	private static final int layout = R.layout.alert_common;

	private View content;
	private String titleMsg, bodyMsg, extendsionMsg, noticeMsg;

	public CommonAlert(String titleMsg, String bodyMsg, String extendsionMsg,
			String noticeMsg, boolean touchClose) {
		if (touchClose)
			dialog = new TouchCloseDialog(controller.getUIContext(),
					new Dismiss());
		this.titleMsg = titleMsg;
		this.bodyMsg = bodyMsg;
		this.extendsionMsg = extendsionMsg;
		this.noticeMsg = noticeMsg;
		content = controller.inflate(layout);
	}

	public void show() {
		setValue();
		super.show(content);
	}

	private void setValue() {
		if (!StringUtil.isNull(titleMsg)) {
			ViewUtil.setRichText(content, R.id.title, titleMsg);
		} else {
			ViewUtil.setGone(content, R.id.title);
			ViewUtil.setGone(content, R.id.titleGap);
		}

		if (!StringUtil.isNull(bodyMsg)) {
			ViewUtil.setRichText(content, R.id.body, bodyMsg);
		} else {
			ViewUtil.setGone(content, R.id.body);
			ViewUtil.setGone(content, R.id.bodyGap);
		}

		if (!StringUtil.isNull(extendsionMsg)) {
			ViewUtil.setRichText(content, R.id.extension, extendsionMsg);
		} else {
			ViewUtil.setGone(content, R.id.extension);
			ViewUtil.setGone(content, R.id.extensionGap);
		}

		if (!StringUtil.isNull(noticeMsg)) {
			ViewUtil.setRichText(content, R.id.notice, noticeMsg);
		} else {
			ViewUtil.setGone(content, R.id.notice);
		}
	}

	private class Dismiss implements CallBack {
		@Override
		public void onCall() {
			dismiss();
		}
	}
}
