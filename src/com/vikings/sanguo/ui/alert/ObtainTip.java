package com.vikings.sanguo.ui.alert;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.AESKeyCache;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.invoker.VerifyInvoker;
import com.vikings.sanguo.message.AccountRestoreResp;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.Home;
import com.vikings.sanguo.utils.MailEditText;
import com.vikings.sanguo.utils.PhoneNumEditText;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.VerifyUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class ObtainTip extends CustomConfirmDialog implements OnClickListener {
	private static final int layout = R.layout.alert_obtain;

	private Button verifyBtn, closeBtn;
	private EditText edit;
	private ListView listView = null;
	private PhoneNumEditText phoneNum;
	private MailEditText mail;

	private int op;

	private SharedPreferences share;
	private boolean reboot = false;

	public ObtainTip(int op, boolean reboot) {
		super(DEFAULT);
		this.reboot = reboot;
		this.op = op;
		share = Config
				.getController()
				.getUIContext()
				.getSharedPreferences(Constants.RESTORE_TAG,
						Context.MODE_PRIVATE);
		edit = (EditText) content.findViewById(R.id.edit);
		if (op == Constants.RESTORE_OP_MAIL_INPUT) {
			edit.setInputType(InputType.TYPE_CLASS_TEXT);
			setTitle("邮箱找回");
			ViewUtil.setText(content, R.id.curtitle, "请输入您与帐号绑定的邮箱");

			mail = new MailEditText(edit,
					(ViewGroup) content.findViewById(R.id.suffix_frame));
			listView = (ListView) content.findViewById(R.id.listView);
			listView.setAdapter(mail.getAdapter());
		} else if (op == Constants.RESTORE_OP_PHONE_INPUT) {
			edit.setInputType(InputType.TYPE_CLASS_PHONE);
			// 设置手机号码长度最长为11，付斌要求的
			edit.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
					PhoneNumEditText.MAX_PHONE_NUM_LEN) });
			setTitle("手机找回");
			ViewUtil.setText(content, R.id.curtitle, "请输入您与帐号绑定的手机号");

			phoneNum = new PhoneNumEditText(edit);
		} else if (op == Constants.RESTORE_OP_VERIFY_CODE_INPUT) {
			edit.setInputType(InputType.TYPE_CLASS_PHONE);
			setTitle("填写验证码");
			ViewUtil.setText(content, R.id.curtitle, "请输入您的验证码");
		}
		verifyBtn = (Button) content.findViewById(R.id.verifyBtn);
		verifyBtn.setOnClickListener(this);
		closeBtn = (Button) content.findViewById(R.id.closeBtn);
		closeBtn.setOnClickListener(closeL);
	}

	@Override
	public void onClick(View v) {
		if (v == verifyBtn) {
			String inputStr = ViewUtil.getText(content, R.id.edit);
			if (!StringUtil.isNull(inputStr)) {
				if (op == Constants.RESTORE_OP_MAIL_INPUT) {
					if (VerifyUtil.checkEmail(inputStr)) {
						new VerifyCodeFetchInvoker("邮箱找回", inputStr).start();
					} else {
						Config.getController().alert("邮箱格式不正确");
					}
				} else if (op == Constants.RESTORE_OP_PHONE_INPUT) {
					inputStr = phoneNum.getText();
					if (VerifyUtil.isMobileNO(inputStr)) {
						new VerifyCodeFetchInvoker("手机找回", inputStr).start();
					} else {
						Config.getController().alert("手机号码格式不正确");
					}
				} else if (op == Constants.RESTORE_OP_VERIFY_CODE_INPUT) {
					if (VerifyUtil.isNumeric(inputStr)) {
						int verifyCode = 0;
						try {
							verifyCode = Integer.valueOf(inputStr);
						} catch (NumberFormatException e) {
						}

						if (verifyCode == 0) {
							controller.alert("请输入有效的验证码");
							return;
						}

						int flag = share.getInt(Constants.RESTORE_FLAG, 0);
						String value = share.getString(Constants.RESTORE_VALUE,
								"");
						if (flag > 0 && !StringUtil.isNull(value)) {
							new RetInvoker(flag, value, verifyCode).start();
						} else {
							Config.getController().alert("请您通过以上两种方式获取验证码");
						}
					} else {
						Config.getController().alert("验证码不能为空");
					}
				}

			} else {
				Config.getController().alert("不能为空");
			}
		}
	}

	// 获取验证码
	private class VerifyCodeFetchInvoker extends BaseInvoker {
		private LoadingTip loadingTip = new LoadingTip();
		private String opStr;
		private int count = 0;
		private String value;

		public VerifyCodeFetchInvoker(String opStr, String value) {
			this.opStr = opStr;
			this.value = value;
		}

		@Override
		protected String failMsg() {
			return opStr;
		}

		@Override
		protected void fire() throws GameException {
			AccountRestoreResp resp = GameBiz.getInstance().accountRestore(op,
					value);
			count = resp.getCount();
			share.edit().putInt(Constants.RESTORE_FLAG, op)
					.putString(Constants.RESTORE_VALUE, value).commit();
			// 由于保存有session，找回后session不过期不重新登录
			AESKeyCache.clear(0);
		}

		@Override
		protected String loadingMsg() {
			return "验证中，请稍后...";
		}

		@Override
		protected void beforeFire() {
			loadingTip.show(loadingMsg());
		}

		@Override
		protected void afterFire() {
			loadingTip.dismiss();
		}

		@Override
		protected void onOK() {
			if (op == Constants.RESTORE_OP_MAIL_INPUT) {
				Config.getController().alert(
						"您的验证码已经发送到指定邮箱，请注意查收<br/>"
								+ StringUtil.color("今日剩余找回次数：" + count, "red"));
			} else if (op == Constants.RESTORE_OP_PHONE_INPUT) {
				Config.getController().alert(
						"您的验证码已经通过短信发送<br/>"
								+ StringUtil.color("今日剩余找回次数：" + count, "red"),
						new CallBack() {

							@Override
							public void onCall() {
								// 自动找回
								new AutoRetrieve("自动找回", controller
										.getResources().getString(
												R.string.verifi_error_msg),
										null, reboot).show();
							}
						});
			}
			dismiss();
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
	protected View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}
}
