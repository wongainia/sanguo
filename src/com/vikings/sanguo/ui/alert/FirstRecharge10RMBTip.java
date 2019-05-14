/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-6-8 下午7:19:44
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.UserVip;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;
import com.vikings.pay.VKConstants;

public class FirstRecharge10RMBTip extends CustomConfirmDialog {

	public FirstRecharge10RMBTip() {
		super("开通VIP送大礼", CustomConfirmDialog.HUGE);
		setRightTopCloseBtn();
		View charge = content.findViewById(R.id.charge);
		if (Config.isCMCC()) {
			ViewUtil.setText(charge, "充值6元立刻开通");
			charge.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
					controller.pay(VKConstants.CHANNEL_CMCC_MM,
							Account.user.getId(), 600, "");
				}
			});
			ViewUtil.setVisible(content, R.id.smNotice);
		} else if (Config.isTelecom()) {
			ViewUtil.setText(charge, "充值5元立刻开通");
			charge.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
					controller.pay(VKConstants.CHANNEL_TELCOM,
							Account.user.getId(), 500, "");
				}
			});
			ViewUtil.setGone(content, R.id.smNotice);
		} else {
			ViewUtil.setGone(content, R.id.smNotice);
			int chargeCount = Account.user.getCharge();
			int left = 0;
			UserVip vip2 = CacheMgr.userVipCache.getVipByLvl(2);
			if (null != vip2)
				left = vip2.getCharge() - chargeCount;
			if (left > 0)
				ViewUtil.setText(charge, "再充值" + (left / Constants.CENT)
						+ "元立刻开通");
			else
				ViewUtil.setText(charge, "去充值立刻开通");
			charge.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
					controller.openRechargeWindow(Account.user.bref());
				}
			});
		}

	}

	@Override
	protected View getContent() {
		return controller.inflate(R.layout.alert_first_recharge_10);
	}

}
