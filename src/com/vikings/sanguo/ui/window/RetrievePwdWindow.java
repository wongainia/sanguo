package com.vikings.sanguo.ui.window;

import android.view.View;
import android.view.View.OnClickListener;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.ui.alert.ObtainTip;
import com.vikings.sanguo.widget.CustomPopupWindow;

public class RetrievePwdWindow extends CustomPopupWindow implements
		OnClickListener {
	private View phoneBtn, mailBtn, verifyBtn;
	private boolean reboot = false;
	private boolean fullScreen = false;


	public RetrievePwdWindow(boolean reboot, boolean fullScreen) {
		this.reboot = reboot;
		this.fullScreen = fullScreen;
	}

	@Override
	protected void init() {
		super.init("找回账号", fullScreen);
		setContent(R.layout.retrieve_pwd);
		setBottomButton("返回", new OnClickListener() {

			@Override
			public void onClick(View v) {
				controller.goBack();

			}
		});

		mailBtn = bindButton(window, R.id.mailBtn, this);
		phoneBtn = bindButton(window, R.id.phoneBtn, this);
		verifyBtn = bindButton(window, R.id.verifyBtn, this);
	}

	@Override
	public void onClick(View v) {
		if (v == mailBtn) {
			new ObtainTip(Constants.RESTORE_OP_MAIL_INPUT, reboot).show();
		} else if (v == phoneBtn) {
			new ObtainTip(Constants.RESTORE_OP_PHONE_INPUT, reboot).show();
		} else if (v == verifyBtn) {
			new ObtainTip(Constants.RESTORE_OP_VERIFY_CODE_INPUT, reboot).show();
		}
	}
}
