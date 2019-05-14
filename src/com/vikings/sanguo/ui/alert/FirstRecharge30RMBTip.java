/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-7-19 下午2:01:34
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
import com.vikings.sanguo.model.UserVip;
import com.vikings.sanguo.thread.ViewImgCallBack;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class FirstRecharge30RMBTip extends CustomConfirmDialog {

	public FirstRecharge30RMBTip() {
		super("开通VIP送大礼", CustomConfirmDialog.HUGE);
		setRightTopCloseBtn();
		View charge = content.findViewById(R.id.charge);

		int chargeCount = Account.user.getCharge();
		UserVip vip4 = CacheMgr.userVipCache.getVipByLvl(4);
		int left = 0;
		if (null != vip4)
			left = vip4.getCharge() - chargeCount;
		if (left > 0) {
			ViewUtil.setText(charge, "再充值" + (left / Constants.CENT) + "元立刻开通");
		} else {
			ViewUtil.setText(charge, "去充值立刻开通");
		}
		new ViewImgCallBack("first_recharge30.jpg",
				content.findViewById(R.id.img));

		ViewUtil.setGone(content, R.id.smNotice);
		charge.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				controller.openRechargeCenterWindow();
			}
		});
	}

	@Override
	protected View getContent() {
		return controller.inflate(R.layout.alert_first_recharge_30, tip, false);
	}
}
