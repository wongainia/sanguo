package com.vikings.sanguo.ui.alert;

import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.invoker.VerifyInvoker;
import com.vikings.sanguo.service.SMSReceiver;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.Home;
import com.vikings.sanguo.ui.ProgressBar;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class AutoRetrieve extends CustomConfirmDialog {

	private SharedPreferences share;

	public static final String ACTION_INTENT_TEST = "android.provider.Telephony.SMS_RECEIVED";

	private static final int layout = R.layout.alert_auto_retrieve;
	private Runnable worker = new Worker();
	private SMSReceiver myReceiver;
	private IntentFilter filter;

	private int second = 60;
	private int wait_time = 0;
	private int temp;
	private int verifyCode;

	private boolean running = true;
	private boolean reboot = false;

	private String failMsg;
	private EditText editText;

	private ProgressBar progressBar;

	public AutoRetrieve(String title, String failMsg, EditText editText,
			boolean reboot) {
		super(DEFAULT);
		this.failMsg = failMsg;
		this.editText = editText;
		this.reboot = reboot;
		progressBar = (ProgressBar) content.findViewById(R.id.progressBar);
		setTitle(title);
		myReceiver = new SMSReceiver();
		filter = new IntentFilter();
		// 优先级
		filter.setPriority(1000);
		// 向过滤器中添加action
		filter.addAction(ACTION_INTENT_TEST);
		// 注册广播
		controller.getUIContext().registerReceiver(myReceiver, filter);
		setButton(SECOND_BTN, "关   闭", closeL);
	}

	public void show() {
		super.show();
		temp = second;
		share = Config
				.getController()
				.getUIContext()
				.getSharedPreferences(Constants.RESTORE_TAG,
						Context.MODE_PRIVATE);
		refresh();
	}

	public void refresh() {
		if (running)
			content.postDelayed(worker, wait_time);
	}

	private class Worker implements Runnable {
		@Override
		public void run() {
			ViewUtil.setVisible(content, R.id.bar);
			progressBar.set(temp--, second);
			ViewUtil.setText(content, R.id.msg, ""
					+ controller.getResources().getString(R.string.verifi_msg)
					+ "(" + temp + "s)");
			verifyCode = Config.verifyCode;
			// 已经取到验证码
			if (verifyCode != 0) {
				controller.getUIContext().unregisterReceiver(myReceiver);
				running = false;
				if (null != editText) {
					dismiss();
					editText.setText(String.valueOf(verifyCode));
				} else {
					int flag = share.getInt(Constants.RESTORE_FLAG, 0);
					String value = share.getString(Constants.RESTORE_VALUE, "");
					if (0 != flag && !StringUtil.isNull(value))
						new RetInvoker(flag, value, verifyCode).start();
				}
				Config.verifyCode = 0;
			}

			// 未取到验证码
			if (temp == 0) {
				ViewUtil.setGone(content, R.id.bar);
				ViewUtil.setText(content, R.id.msg, failMsg);
			} else {
				wait_time = 1000;
				refresh();
			}
		}
	}

	private class RetInvoker extends VerifyInvoker {

		public RetInvoker(int flag, String value, int verifyCode) {
			super(flag, value, verifyCode);
		}

		@Override
		protected void onOK() {
			if (singleId()) {
				share.edit().clear().commit();
				dismiss();
				Config.getController().alert("验证成功", getCallBack());
			} else {
				// 多账号
				dismiss();
				new UserChooseTip(resp.getInfos(), getCallBack()).show();
			}
		}
	}

	@Override
	public void dismiss() {
		super.dismiss();
		// 注销广播
		if (running)
			controller.getUIContext().unregisterReceiver(myReceiver);
		running = false;
	}

	private CallBack getCallBack() {
		return new CallBack() {
			@Override
			public void onCall() {
				if (reboot) {
					controller.reboot();
				} else {
					controller.goBack();
					Home home = (Home) Config.getGameUI("home");
					home.enter();
				}
			}
		};
	}

	@Override
	protected void doOnDismiss() {
		if (verifyCode == 0 && null == editText)
			new ObtainTip(Constants.RESTORE_OP_VERIFY_CODE_INPUT, reboot)
					.show();
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout);
	}
}
