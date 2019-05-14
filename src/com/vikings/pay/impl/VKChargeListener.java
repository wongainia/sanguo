package com.vikings.pay.impl;

import android.view.Gravity;

import com.vikings.sanguo.config.Config;
import com.vikings.pay.OnChargeSubmitListener;
import com.vikings.pay.VKConstants;

public class VKChargeListener implements OnChargeSubmitListener {

	@Override
	public void onSubmitOrder(String orderId, boolean ok, int channel,
			String error) {
		Config.getController().dismissLoading();
		if (ok) {
			if (channel != VKConstants.CHANNEL_TELCOM)
				Config.getController().alert("系统提示， 正在充值…",
						"充值结果将会显示在系统消息中，请注意查收", null, true);
		} else {
			Config.getController().alert("", "充值失败:" + error, Gravity.LEFT, "",
					null, true);
		}
	}

}
