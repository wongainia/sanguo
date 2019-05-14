/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2012-7-5
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */

package com.vikings.sanguo.ui.window;

import android.view.View;
import android.view.View.OnClickListener;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.RechargeState;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class RechargeResultTip extends CustomConfirmDialog implements
		OnClickListener {
	private View recharge;
	private View back;
	private byte type; // 充值方式：支付宝，移动/联通/电信充值卡

	public RechargeResultTip(BriefUserInfoClient user, int amount, byte type) {
		super("充值结果", CustomConfirmDialog.DEFAULT);
		this.type = type;

		String str = amount
				+ "元宝将被充值到"
				+ (Account.user.equals(user) ? "您" : (user.getNickName() + "("
						+ user.getId() + ")")) + "的账户";

		ViewUtil.setBoldText(content.findViewById(R.id.content), str);

		recharge = bindButton(content, R.id.recharge, this);
		back = bindButton(content, R.id.back, this);
	}

	@Override
	public void onClick(View v) {
		if (v == recharge) {
			dismiss();

			switch (type) {
			case RechargeState.ID_ALIPAY:
				break;
			case RechargeState.ID_CHINA_MOBILE_CARD:
			case RechargeState.ID_CHINA_UNICOM_CARD:
			case RechargeState.ID_CHINA_TELECOM_CARD:
				controller.goBack();
				break;
			default:
				break;
			}
		} else if (v == back) {
			dismiss();
		}
	}

	@Override
	protected View getContent() {
		return controller.inflate(R.layout.recharge_result);
	}
}
