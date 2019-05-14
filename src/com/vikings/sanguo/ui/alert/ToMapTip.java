package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;

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

public class ToMapTip extends CustomConfirmDialog {
	private View recharge;
	private UserVip needVip;

	public ToMapTip() {
		super("世界征战", DEFAULT);
		recharge = findViewById(R.id.recharge);
		ViewUtil.setImage(content.findViewById(R.id.toMap), "to_map");
		setRightTopCloseBtn();
	}

	public void show() {
		needVip = CacheMgr.userVipCache.getVipByLvl(1);
		if (needVip == null)
			return;
		setValue();
		super.show();
	}

	private void setValue() {
		int charge = Account.user.getCharge();
		int left = needVip.getCharge() - charge;

		if (Config.isCMCC() || Config.isTelecom() || Config.isCUCC()) {
			if (needVip.getSmChargeRate() > 0) {
				final int amount = CalcUtil.upNum(1f * left
						/ needVip.getSmChargeRate());
				if (VKSkyPay.isValidAmount(amount)) {
					ViewUtil.setText(recharge, "充值" + amount + "元送VIP1立获神将吕布");
					recharge.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							dismiss();
							controller.pay(VKConstants.CHANNEL_SKYPAY,
									Account.user.getId(), amount, ""
											+ Account.user.getIdCardNumber());
						}
					});
				} else {
					setRewardCharge(left);
				}
			} else {
				setRewardCharge(left);
			}
		} else {
			setRewardCharge(left);
		}

	}

	protected void setRewardCharge(int left) {
		int amount = CalcUtil.upNum(1f * left / Constants.CENT);
		if (needVip.getChargeRate() > 0)
			amount = CalcUtil.upNum(1f * left / needVip.getChargeRate());

		ViewUtil.setText(recharge, "充值" + amount + "元送VIP1立获神将吕布");
		recharge.setOnClickListener(new OnClickListener() {

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
		return controller.inflate(R.layout.alert_to_map, contentLayout, false);
	}

}
