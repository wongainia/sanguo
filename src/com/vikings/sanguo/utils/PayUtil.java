package com.vikings.sanguo.utils;

import android.view.Gravity;

import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.invoker.BuyAndOpenGiftBackInvoker;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.RechargeOrderData;
import com.vikings.sanguo.model.SyncData;
import com.vikings.sanguo.model.UserAccountClient;
import com.vikings.pay.OnChargeQueryListener;
import com.vikings.pay.OnChargeSubmitListener;
import com.vikings.pay.VKConstants;

public class PayUtil {

	public static RechargeOrderData data = null;

	/**
	 * 判断是否充值过， 如果充值过，需要重新查询累计充值额度
	 */
	public static void checkIfCharged(SyncData<UserAccountClient> userInfo) {
		if (userInfo == null)
			return;
		if (userInfo.getData() == null)
			return;
		// 元宝变化超过100
		if (userInfo.getData().getCurrency() - Account.user.getCurrency() > 50) {
			if (data != null && data.getItemId() > 0)
				Config.getController()
						.getVKPayMgr()
						.query(data.getOrderId(),
								new MyOnChargeQueryListener(data.getItemId()));
		}
	}

	public static void pay(int channel, int userId, int amount, String exParam,
			int itemId) {
		Config.getController().showLoading("订单提交中...");
		Config.getController()
				.getVKPayMgr()
				.pay(channel, userId, amount, exParam,
						new MyOnChargeSubmitListener(itemId));
	}

	private static class MyOnChargeSubmitListener implements
			OnChargeSubmitListener {

		private int itemId;

		public MyOnChargeSubmitListener(int itemId) {
			this.itemId = itemId;
		}

		@Override
		public void onSubmitOrder(String orderId, boolean ok, int channel,
				String error) {
			Config.getController().dismissLoading();
			if (ok) {
				if (itemId > 0) {
					data = new RechargeOrderData();
					data.setOrderId(orderId);
					data.setItemId(itemId);
				}
				if (channel != VKConstants.CHANNEL_TELCOM)
					Config.getController().alert("系统提示， 正在充值…");
			} else {
				Config.getController().alert("", "充值失败:" + error, Gravity.LEFT,
						"", null, true);
			}
		}
	}

	private static class MyOnChargeQueryListener implements
			OnChargeQueryListener {

		private int itemId;

		public MyOnChargeQueryListener(int itemId) {
			this.itemId = itemId;
		}

		@Override
		public void onQueryResult(String orderId, boolean ok, String error) {
			if (ok && itemId > 0) {
				Item item = CacheMgr.getItemByID(itemId);
				if (null != item)
					new BuyAndOpenGiftBackInvoker(item).start();
				data = null;
			}
		}
	}

}
