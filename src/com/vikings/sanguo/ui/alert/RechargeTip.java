package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.vikings.pay.VKConstants;
import com.vikings.pay.VKSkyPay;
import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.UserVip;
import com.vikings.sanguo.ui.window.RechargeWindow;
import com.vikings.sanguo.utils.CalcUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class RechargeTip extends CustomConfirmDialog {
	private static final int layout = R.layout.alert_recharge;
	private TextView desc, ext, smNotice;
	private Button rechargeBtn;

	private String descStr, extStr;
	private UserVip needVip;

	public RechargeTip(String descStr, String extStr) {
		super("友情提示", DEFAULT);
		setRightTopCloseBtn();
		this.descStr = descStr;
		this.extStr = extStr;
		desc = (TextView) content.findViewById(R.id.desc);
		ext = (TextView) content.findViewById(R.id.ext);
		smNotice = (TextView) content.findViewById(R.id.smNotice);
		rechargeBtn = (Button) content.findViewById(R.id.rechargeBtn);
	}

	public void show() {
		needVip = CacheMgr.userVipCache.getVipByLvl(1);
		if (needVip == null)
			return;
		setValue();
		super.show();
	}

	private void setValue() {
		ViewUtil.setText(desc, descStr);
		int charge = Account.user.getCharge();
		int left = needVip.getCharge() - charge;
		if (needVip.getSmChargeRate() > 0) {
			if (Config.isCMCC() || Config.isTelecom() || Config.isCUCC()) {

				final int amount = CalcUtil.upNum(1f * left
						/ needVip.getSmChargeRate());
				if (VKSkyPay.isValidAmount(amount)) {
					extStr = extStr.replace("<param0>", "仅需" + amount);
					rechargeBtn.setText("充值" + amount + "元立刻开通VIP");
					rechargeBtn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							dismiss();
							controller.pay(VKConstants.CHANNEL_SKYPAY,
									Account.user.getId(), amount, ""
											+ Account.user.getIdCardNumber());
						}
					});

					ViewUtil.setVisible(smNotice);
					ViewUtil.setText(smNotice, "将从您的手机中扣除" + amount + "元话费");
				} else {
					setRewardCharge(left);
					ViewUtil.setGone(smNotice);
				}

			} else {
				setRewardCharge(left);
				ViewUtil.setGone(smNotice);
			}

		} else {
			setRewardCharge(left);
			ViewUtil.setGone(smNotice);
		}
		ViewUtil.setRichText(ext, extStr, true);
	}

	protected void setRewardCharge(int left) {
		int amount = CalcUtil.upNum(1f * left / Constants.CENT);
		if (needVip.getChargeRate() > 0)
			amount = CalcUtil.upNum(1f * left / needVip.getChargeRate());

		extStr = extStr.replace("<param0>", "仅需" + amount);
		rechargeBtn.setText("充值" + amount + "元立刻开通VIP");
		rechargeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				controller.openRechargeWindow(
						RechargeWindow.TYPE_REWARD_RECHARGE,
						Account.user.bref());
			}
		});
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}

}
