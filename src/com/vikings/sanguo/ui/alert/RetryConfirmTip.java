package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

//TODO 废弃
public class RetryConfirmTip extends CustomConfirmDialog { // Alert

	private static final int layout = R.layout.alert_confirm_retry;
	protected TextView msg, msgExt;
	private String msgStr, msgExtStr;

	public RetryConfirmTip(String msgStr, String msgExtStr, String okBtnStr,
			final CallBack okCallBack, String cancelBtnStr,
			final CallBack cancelCallBack) {
		super();
		this.msgStr = msgStr;
		this.msgExtStr = msgExtStr;
		setButton(SECOND_BTN, okBtnStr, new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (null != okCallBack)
					okCallBack.onCall();
			}
		});
		setButton(THIRD_BTN, cancelBtnStr, new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (null != cancelCallBack)
					cancelCallBack.onCall();
			}
		});
		msg = (TextView) content.findViewById(R.id.msg);
		msgExt = (TextView) content.findViewById(R.id.msgExt);
	}

	private void setContent(String msgStr, String msgExtStr) {
		ViewUtil.setRichText(msg, msgStr);
		if (StringUtil.isNull(msgExtStr)) {
			ViewUtil.setGone(msgExt);
		} else {
			ViewUtil.setVisible(msgExt);
			ViewUtil.setRichText(msgExt, msgExtStr);
		}
	}

	public void show() {
		setContent(msgStr, msgExtStr);
		super.show();
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}
}
