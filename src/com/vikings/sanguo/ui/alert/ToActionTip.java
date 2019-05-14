package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;

import com.vikings.pay.VKConstants;
import com.vikings.pay.VKSkyPay;
import com.vikings.pay.VKTelcom;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class ToActionTip extends CustomConfirmDialog {
	private final static int layout = R.layout.alert_toplant;
	private int needCount;

	public ToActionTip(int count) {
		super("元宝不足", CustomConfirmDialog.DEFAULT);
		this.needCount = count;
	}

	public void show() {
		setValue();
		super.show();
	}

	private void setValue() {
		ViewUtil.setVisible(content, R.id.msg1);
		ViewUtil.setRichText(content, R.id.msg1, "您的元宝余额不足，请先充值");
		if (Config.isCMCC() || Config.isCUCC()) {
			final int amount = getRechargeAmount(needCount);
			if (needCount <= amount) {
				setButton(CustomConfirmDialog.FIRST_BTN,
						(amount / 100) + "元充值", new OnClickListener() {
							@Override
							public void onClick(View v) {
								dismiss();

								controller.pay(VKConstants.CHANNEL_SKYPAY,
										Account.user.getId(), amount, "");
							}
						});
				ViewUtil.setVisible(content, R.id.smNotice);
			} else {
				setButtonToRechargeCenter();
			}
		} else if (Config.isTelecom()) {
			final int amount = VKTelcom.getAmount(needCount);
			if (needCount <= amount) {
				setButton(CustomConfirmDialog.FIRST_BTN,
						(amount / 100) + "元充值", new OnClickListener() {
							@Override
							public void onClick(View v) {
								dismiss();

								controller.pay(VKConstants.CHANNEL_SKYPAY,
										Account.user.getId(), amount, "");

							}
						});
				ViewUtil.setGone(content, R.id.smNotice);
			} else {
				setButtonToRechargeCenter();
			}
		} else {
			setButtonToRechargeCenter();
		}
		setButton(CustomConfirmDialog.SECOND_BTN, "关闭", closeL);

	}

	protected void setButtonToRechargeCenter() {
		setButton(CustomConfirmDialog.FIRST_BTN, "去  充  值",
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						dismiss();
						controller.openRechargeWindow(Account.user.bref());
					}
				});
		ViewUtil.setGone(content, R.id.smNotice);
	}

	// price的单位是分，所以和元宝数是相等的
	private int getRechargeAmount(int count) {
		int[] amounts = VKSkyPay.getAmount();
		for (int i = 0; i < amounts.length; i++) {
			if (count <= amounts[i])
				return amounts[i];
		}
		return amounts[amounts.length - 1];
	}

	@Override
	public View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}

}
