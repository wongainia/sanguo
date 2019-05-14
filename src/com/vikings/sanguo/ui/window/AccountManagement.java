package com.vikings.sanguo.ui.window;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.ui.alert.BindTip;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomPopupWindow;

public class AccountManagement extends CustomPopupWindow implements
		OnClickListener {
	private ViewGroup emailLayout, phoneLayout, idCardLayout;
	private Button emailBtn, phoneBtn, idCardBtn;
	private TextView securityLevel, email, phone, idCard;

	public void show() {
		doOpen();
	}

	@Override
	protected void init() {
		super.init("管理账号");
		setContent(R.layout.account_mgt_view);

		emailLayout = (ViewGroup) window.findViewById(R.id.emailLayout);
		phoneLayout = (ViewGroup) window.findViewById(R.id.phoneLayout);
		idCardLayout = (ViewGroup) window.findViewById(R.id.idCardLayout);

		securityLevel = (TextView) window.findViewById(R.id.securityLevel);
		email = (TextView) window.findViewById(R.id.email);
		phone = (TextView) window.findViewById(R.id.phone);
		idCard = (TextView) window.findViewById(R.id.idCard);
		emailBtn = (Button) window.findViewById(R.id.emailBtn);
		emailBtn.setOnClickListener(this);
		phoneBtn = (Button) window.findViewById(R.id.phoneBtn);
		phoneBtn.setOnClickListener(this);
		idCardBtn = (Button) window.findViewById(R.id.idCardBtn);
		idCardBtn.setOnClickListener(this);
	}

	@Override
	public void showUI() {
		super.showUI();
		setValue();
	}

	private void setValue() {
		String statusStr = "<font color='#FD0A0A'>" + "不安全" + "</font>";
		String emailStr = Account.user.getEmail();
		if (StringUtil.isNull(emailStr)) {
			ViewUtil.setImage(emailLayout.findViewById(R.id.state),
					R.drawable.cha);
			ViewUtil.setRichText(email,
					StringUtil.color("未绑定邮箱", R.color.color24));
			ViewUtil.setText(emailBtn, "绑定");
		} else {
			ViewUtil.setImage(emailLayout.findViewById(R.id.state),
					R.drawable.gou);

			int pos = emailStr.indexOf('@');

			ViewUtil.setRichText(
					email,
					(pos > 2 ? emailStr.substring(0, 3) : emailStr.substring(0,
							pos))
							+ "****"
							+ emailStr.substring(pos, emailStr.length()));
			statusStr = "<font color='#FA7B49'>" + "低" + "</font>";
			ViewUtil.setText(emailBtn, "修改");
		}

		String phoneStr = Account.user.getMobile();
		if (StringUtil.isNull(phoneStr)) {
			ViewUtil.setImage(phoneLayout.findViewById(R.id.state),
					R.drawable.cha);
			ViewUtil.setRichText(phone,
					StringUtil.color("未绑定手机", R.color.color24));
			ViewUtil.setText(phoneBtn, "绑定");
		} else {
			ViewUtil.setImage(phoneLayout.findViewById(R.id.state),
					R.drawable.gou);
			ViewUtil.setRichText(
					phone,
					phoneStr.substring(0, 3)
							+ "****"
							+ phoneStr.substring(phoneStr.length() - 4,
									phoneStr.length()));
			statusStr = "<font color='#E0E101'>" + "中" + "</font>";
			ViewUtil.setText(phoneBtn, "修改");
		}

		String idCardStr = Account.user.getIdCardNumber();
		if (StringUtil.isNull(idCardStr)) {
			ViewUtil.setImage(idCardLayout.findViewById(R.id.state),
					R.drawable.cha);
			ViewUtil.setRichText(idCard,
					StringUtil.color("未实名认证", R.color.color24));
			ViewUtil.setText(idCardBtn, "认证");
		} else {
			ViewUtil.setImage(idCardLayout.findViewById(R.id.state),
					R.drawable.gou);
			ViewUtil.setText(idCard, "已通过实名认证");
			statusStr = "<font color='#25f111'>" + "高" + "</font>";
			ViewUtil.setGone(idCardBtn);
		}

		ViewUtil.setRichText(securityLevel, statusStr);
	}

	@Override
	public void onClick(View v) {
		if (v == emailBtn) {
			if (StringUtil.isNull(Account.user.getEmail())) {
				new BindTip(BindTip.BIND_EMAIL).show();
			} else {
				if (StringUtil.isNull(Account.user.getMobile())) {
					controller.alert("必须先绑定手机，才可以修改邮箱");
					return;
				}
				new BindTip(BindTip.CHANGE_EMAIL).show();
			}
		} else if (v == phoneBtn) {
			if (StringUtil.isNull(Account.user.getMobile())) {
				new BindTip(BindTip.BIND_PHONE).show();
			} else {
				new BindTip(BindTip.CHANGE_PHONE).show();
			}
		} else if (v == idCardBtn) {
			if (StringUtil.isNull(Account.user.getMobile())) {
				controller.alert("必须先绑定手机，才可以申请【实名认证】");
				return;
			}
			new BindTip(BindTip.BIND_ID_CARD).show();
		}
	}
}
