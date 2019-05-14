package com.vikings.sanguo.ui.alert;

import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.message.AccountBinding2Resp;
import com.vikings.sanguo.protos.BindingType;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.MailEditText;
import com.vikings.sanguo.utils.PhoneNumEditText;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.VerifyUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class BindTip extends CustomConfirmDialog implements OnClickListener,
		CallBack {
	public static final int BIND_EMAIL = 1;
	public static final int CHANGE_EMAIL = 2;
	public static final int BIND_PHONE = 3;
	public static final int CHANGE_PHONE = 4;
	public static final int BIND_ID_CARD = 5;

	private static final int layout = R.layout.alert_bind;
	private View step1Group, step2Group, step3Group;
	private Button bindBtn1, bindBtn2, bindBtn3, step1Btn, step2Btn, step3Btn;
	private TextView step1Num, step1Text, step2Num, step2Text, step3Num,
			step3Text, fillview;
	private EditText step1Edit, step2Edit, step3Edit;
	private ViewGroup suffixFrame;
	private ListView listView = null;

	private CallBack callBack;
	private int operType;
	private PhoneNumEditText phoneNum;
	private MailEditText mail;

	public BindTip(int operType) {
		super(getScale(operType));
		this.operType = operType;

		step1Group = content.findViewById(R.id.step1Group);
		step1Num = (TextView) content.findViewById(R.id.step1Num);
		step1Text = (TextView) content.findViewById(R.id.step1Text);
		fillview = (TextView) content.findViewById(R.id.fillview);
		step1Edit = (EditText) content.findViewById(R.id.step1Edit);
		step1Btn = (Button) content.findViewById(R.id.step1Btn);
		step1Btn.setOnClickListener(this);
		bindBtn1 = (Button) content.findViewById(R.id.bindBtn1);
		bindBtn1.setOnClickListener(this);

		step2Group = content.findViewById(R.id.step2Group);
		step2Num = (TextView) content.findViewById(R.id.step2Num);
		step2Text = (TextView) content.findViewById(R.id.step2Text);
		step2Edit = (EditText) content.findViewById(R.id.step2Edit);
		step2Btn = (Button) content.findViewById(R.id.step2Btn);
		step2Btn.setOnClickListener(this);
		bindBtn2 = (Button) content.findViewById(R.id.bindBtn2);
		bindBtn2.setOnClickListener(this);

		step3Group = content.findViewById(R.id.step3Group);
		step3Num = (TextView) content.findViewById(R.id.step3Num);
		step3Text = (TextView) content.findViewById(R.id.step3Text);
		step3Edit = (EditText) content.findViewById(R.id.step3Edit);
		step3Btn = (Button) content.findViewById(R.id.step3Btn);
		step3Btn.setOnClickListener(this);
		bindBtn3 = (Button) content.findViewById(R.id.bindBtn3);
		bindBtn3.setOnClickListener(this);

		suffixFrame = (ViewGroup) content.findViewById(R.id.suffix_frame);

		if (operType == BIND_EMAIL || operType == CHANGE_EMAIL) {
			mail = new MailEditText(step1Edit, suffixFrame);
			listView = (ListView) content.findViewById(R.id.listView);
			listView.setAdapter(mail.getAdapter());
		} else if (operType == BIND_PHONE || operType == CHANGE_PHONE) {
			phoneNum = new PhoneNumEditText(step1Edit);
		}
		setCloseBtn();
	}

	private static int getScale(int operType) {
		if (operType == BIND_EMAIL)
			return DEFAULT;
		else
			return LARGE;
	}

	public void show() {
		setValue();
		super.show();
	}

	private void setValue() {

		ViewUtil.setRichText(step1Num, "步骤一");
		if (operType == BIND_EMAIL) {
			setTitle("绑定邮箱");
			ViewUtil.setText(step1Text, "输入你想绑定的电子邮箱地址，并点击下方的【确认绑定】按钮");
			ViewUtil.setVisible(fillview);
			ViewUtil.setGone(step1Btn);
			ViewUtil.setGone(step2Group);
			ViewUtil.setGone(step3Group);
			ViewUtil.setRichText(bindBtn1, "确认绑定");// StringUtil.color("确认绑定",
													// R.color.color3)

			LayoutParams params = (LayoutParams) suffixFrame.getLayoutParams();
			params.setMargins(0, (int) (140 * Config.SCALE_FROM_HIGH), 0, 0);
			suffixFrame.setLayoutParams(params);
		} else if (operType == CHANGE_EMAIL) {
			setTitle("更改邮箱");
			ViewUtil.setText(step1Text, "输入想绑定的新电子邮箱地址");
			ViewUtil.setGone(step1Btn);
			ViewUtil.setGone(bindBtn1);
			ViewUtil.setRichText(step2Num, "步骤二");
			ViewUtil.setText(step2Text, "点击【获取验证码】按钮，您绑定的手机将收到带有验证码的短信");
			ViewUtil.setGone(step2Edit);
			ViewUtil.setGone(bindBtn2);
			ViewUtil.setRichText(step3Num, "步骤三");
			ViewUtil.setText(step3Text, "输入您所收到的手机短信中的验证码，点击【确定修改】");
			ViewUtil.setGone(step3Btn);
			step3Edit.setInputType(InputType.TYPE_CLASS_NUMBER);
			ViewUtil.setText(bindBtn3, "确定修改");// StringUtil.color("确定修改",
												// R.color.color3)

			LayoutParams params = (LayoutParams) suffixFrame.getLayoutParams();
			params.setMargins(0, (int) (120 * Config.SCALE_FROM_HIGH), 0, 0);
			suffixFrame.setLayoutParams(params);
		} else if (operType == BIND_PHONE) {
			setTitle("绑定手机");
			ViewUtil.setText(step1Text, "输入你想绑定的手机号码，并点击下方的【获取验证码】按钮");
			step1Edit.setInputType(InputType.TYPE_CLASS_PHONE);
			step1Edit
					.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
							PhoneNumEditText.MAX_PHONE_NUM_LEN) });
			ViewUtil.setRichText(step2Num, "步骤二");
			ViewUtil.setText(step2Text, " 输入您收到的短信中的验证码，之后点击【确认绑定】按钮");
			step2Edit.setInputType(InputType.TYPE_CLASS_NUMBER);
			ViewUtil.setGone(step2Btn);
			ViewUtil.setGone(bindBtn1);
			ViewUtil.setGone(step3Num);

			ViewUtil.setGone(step3Group);
			ViewUtil.setText(bindBtn2, "确认绑定");
		} else if (operType == CHANGE_PHONE) {
			setTitle("更改手机");
			ViewUtil.setText(step1Text, "输入想修改的新手机号码");
			step1Edit.setInputType(InputType.TYPE_CLASS_PHONE);
			step1Edit
					.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
							PhoneNumEditText.MAX_PHONE_NUM_LEN) });
			ViewUtil.setRichText(step2Num, "步骤二");
			ViewUtil.setGone(step1Btn);
			ViewUtil.setGone(bindBtn1);
			ViewUtil.setText(step2Text, "点击【获取验证码】按钮，您绑定的手机将收到带有验证码的短信");
			ViewUtil.setGone(step2Edit);
			ViewUtil.setGone(bindBtn2);
			ViewUtil.setRichText(step3Num, "步骤三");
			ViewUtil.setText(step3Text, "输入您所收到的手机短信中的验证码，点击【确定修改】");
			ViewUtil.setGone(step3Btn);
			step3Edit.setInputType(InputType.TYPE_CLASS_NUMBER);
			ViewUtil.setText(bindBtn3, "确定修改");
		} else if (operType == BIND_ID_CARD) {
			setTitle("实名认证");
			ViewUtil.setText(step1Text, "正确输入您的身份证号码");
			ViewUtil.setGone(step1Btn);
			ViewUtil.setGone(bindBtn1);
			ViewUtil.setRichText(step2Num, "步骤二");
			ViewUtil.setText(step2Text, "点击【获取验证码】，您所绑定的手机号将收到官方发送的验证码短信");
			ViewUtil.setGone(step2Edit);
			ViewUtil.setGone(bindBtn2);
			ViewUtil.setRichText(step3Num, "步骤三");
			ViewUtil.setText(step3Text, "输入您所收到的手机短信中的验证码");
			ViewUtil.setGone(step3Btn);
			step3Edit.setInputType(InputType.TYPE_CLASS_NUMBER);
			ViewUtil.setText(bindBtn3, "确认绑定");
		}
		ViewUtil.setImage(content, R.id.poto_right1,
				ImageUtil.getMirrorBitmapDrawable("potpourri_bg"));
		ViewUtil.setImage(content, R.id.poto_right2,
				ImageUtil.getMirrorBitmapDrawable("potpourri_bg"));
		ViewUtil.setImage(content, R.id.poto_right3,
				ImageUtil.getMirrorBitmapDrawable("potpourri_bg"));
	}

	@Override
	public void onClick(View v) {
		if (v == bindBtn1 || v == bindBtn2 || v == bindBtn3) {
			if (operType == BIND_EMAIL) {
				String mailStr = ViewUtil.getText(content, R.id.step1Edit);
				// 校验
				if (!checkEmail(mailStr))
					return;
				new BindInvoker(BindingType.BINDING_EMAIL, BIND_EMAIL, mailStr,
						0, 0).start();
			} else if (operType == CHANGE_EMAIL) {
				String mailStr = ViewUtil.getText(content, R.id.step1Edit);
				String codeStr = ViewUtil.getText(content, R.id.step3Edit);
				if (!checkEmail(mailStr))
					return;
				if (!checkVerCode(codeStr))
					return;
				new BindInvoker(BindingType.BINDING_EMAIL, CHANGE_EMAIL,
						mailStr, Integer.valueOf(codeStr), 0).start();
			} else if (operType == BIND_PHONE) {
				String phoneStr = phoneNum.getText();
				String codeStr = ViewUtil.getText(content, R.id.step2Edit);
				String inviterStr = ViewUtil.getText(content, R.id.step3Edit);
				if (!checkPhone(phoneStr))
					return;
				if (!checkVerCode(codeStr))
					return;

				try {
					if (!StringUtil.isNull(inviterStr))
						Integer.parseInt(inviterStr);
				} catch (Exception e) {
					controller.alert("邀请者ID格式不正确");
					return;
				}

				if (StringUtil.isNull(inviterStr))
					new BindInvoker(BindingType.BINDING_MOBILE, BIND_PHONE,
							phoneStr, Integer.valueOf(codeStr), 0).start();
				else
					new BindInvoker(BindingType.BINDING_MOBILE, BIND_PHONE,
							phoneStr, Integer.valueOf(codeStr),
							Integer.valueOf(inviterStr)).start();
			} else if (operType == CHANGE_PHONE) {
				String phoneStr = phoneNum.getText();
				String codeStr = ViewUtil.getText(content, R.id.step3Edit);
				if (!checkPhone(phoneStr))
					return;
				if (!checkVerCode(codeStr))
					return;
				new BindInvoker(BindingType.BINDING_MOBILE, CHANGE_PHONE,
						phoneStr, Integer.valueOf(codeStr), 0).start();

			} else if (operType == BIND_ID_CARD) {
				String idCardStr = ViewUtil.getText(content, R.id.step1Edit);
				String codeStr = ViewUtil.getText(content, R.id.step3Edit);
				if (!checkIDCard(idCardStr))
					return;
				if (!checkVerCode(codeStr))
					return;
				new BindInvoker(BindingType.BINDING_IDCARD, BIND_ID_CARD,
						idCardStr, Integer.valueOf(codeStr), 0).start();

			}
		} else if (v == step1Btn) {
			if (operType == BIND_PHONE) {
				String phoneStr = phoneNum.getText();// ViewUtil.getText(content,
														// R.id.step1Edit);
				// 校验
				if (!checkPhone(phoneStr))
					return;
				new FetchVerCode(BindingType.BINDING_MOBILE, phoneStr).start();
			}
		} else if (v == step2Btn) {
			if (operType == CHANGE_EMAIL) {
				String mailStr = ViewUtil.getText(content, R.id.step1Edit);
				if (!checkEmail(mailStr))
					return;
				new FetchVerCode(BindingType.BINDING_EMAIL, mailStr).start();
			} else if (operType == CHANGE_PHONE) {
				String phoneStr = phoneNum.getText();
				if (!checkPhone(phoneStr))
					return;
				new FetchVerCode(BindingType.BINDING_MOBILE, phoneStr).start();
			} else if (operType == BIND_ID_CARD) {
				String idCardStr = ViewUtil.getText(content, R.id.step1Edit);
				if (!checkIDCard(idCardStr))
					return;
				new FetchVerCode(BindingType.BINDING_IDCARD, idCardStr).start();
			}
		}
	}

	// 检查邮箱
	private boolean checkEmail(String mailStr) {
		if (StringUtil.isNull(mailStr)) {
			controller.alert("邮箱不能为空");
			return false;
		}
		if (!VerifyUtil.checkEmail(mailStr)) {
			controller.alert("输入邮箱格式不正确");
			return false;
		}
		return true;
	}

	// 校验验证码
	private boolean checkVerCode(String codeStr) {
		if (StringUtil.isNull(codeStr)) {
			controller.alert("验证码不能为空");
			return false;
		}
		if (!VerifyUtil.isNumeric(codeStr)) {
			controller.alert("验证码格式不正确");
			return false;
		}
		try {
			Integer.parseInt(codeStr);
		} catch (Exception e) {
			controller.alert("验证码不正确");
			return false;
		}
		return true;
	}

	// 校验身份证
	private boolean checkIDCard(String idCardStr) {
		if (StringUtil.isNull(idCardStr)) {
			controller.alert("身份证号码不能为空");
			return false;
		}
		if (!VerifyUtil.isIDCard(idCardStr)) {
			controller.alert("身份证号码格式不正确");
			return false;
		}
		return true;
	}

	// 校验手机号
	private boolean checkPhone(String phoneStr) {
		if (StringUtil.isNull(phoneStr)) {
			controller.alert("手机号码不能为空");
			return false;
		}
		if (!VerifyUtil.isMobileNO(phoneStr)) {
			controller.alert("手机号码格式不正确");
			return false;
		}
		return true;
	}

	private class BindInvoker extends BaseInvoker {

		private BindingType type;
		private int clientType;
		private String value;
		private int code;
		private int inviter;

		private AccountBinding2Resp resp;

		public BindInvoker(BindingType type, int clientType, String value,
				int code, int inviter) {
			this.type = type;
			this.clientType = clientType;
			this.value = value;
			this.code = code;
			this.inviter = inviter;
		}

		@Override
		protected String loadingMsg() {
			return "正在绑定，请稍后...";
		}

		@Override
		protected String failMsg() {
			return "绑定失败";
		}

		@Override
		protected void fire() throws GameException {
			resp = GameBiz.getInstance().accountBinding2(type, value, code,
					inviter);
		}

		@Override
		protected void onOK() {
			ctr.updateUI(resp.getRi(), true);
			if (clientType == BIND_EMAIL || clientType == CHANGE_EMAIL) {
				Account.user.setEmail(value);
			} else if (clientType == BIND_PHONE || clientType == CHANGE_PHONE) {
				Account.user.setMobile(value);

			} else if (clientType == BIND_ID_CARD) {
				Account.user.setIdCardNumber(value);
			}
			dismiss();
			new BoundChangeSuccess().show(clientType);
			CacheMgr.userCache.updateCache(Account.user.bref());
			if (null != callBack)
				callBack.onCall();
		}

	}

	private class FetchVerCode extends BaseInvoker {
		private BindingType type;
		private String value;

		public FetchVerCode(BindingType type, String value) {
			this.type = type;
			this.value = value;
		}

		@Override
		protected String loadingMsg() {
			return "获取验证码…";
		}

		@Override
		protected String failMsg() {
			return "获取验证码失败";
		}

		@Override
		protected void fire() throws GameException {
			GameBiz.getInstance().accountBinding(type, value);
		}

		@Override
		protected void onOK() {
			controller.alert("您的验证码已经通过短信发送", BindTip.this);
		}

	}

	@Override
	public void onCall() {
		if (operType == CHANGE_EMAIL) {
			new AutoRetrieve("获取验证码", "验证码未收到，稍后请在收到验证码短信时，手动填写验证码", step3Edit,
					false).show();
		} else if (operType == BIND_PHONE) {
			new AutoRetrieve("获取验证码", "验证码未收到，稍后请在收到验证码短信时，手动填写验证码", step2Edit,
					false).show();
		} else if (operType == CHANGE_PHONE) {
			new AutoRetrieve("获取验证码", "验证码未收到，稍后请在收到验证码短信时，手动填写验证码", step3Edit,
					false).show();
		} else if (operType == BIND_ID_CARD) {
			new AutoRetrieve("获取验证码", "验证码未收到，稍后请在收到验证码短信时，手动填写验证码", step3Edit,
					false).show();
		}
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}
}
