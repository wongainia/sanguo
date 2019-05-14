package com.vikings.pay;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.alipay.SecurePayHelperUnionpay;
import com.vikings.sanguo.config.Config;

public class VKUnionpay extends VKPayService {


	private SecurePayHelperUnionpay spHelper;

	private Activity activity;


	public VKUnionpay(int game, Activity activity) {
		super(game);
		this.activity = activity;
		spHelper = new SecurePayHelperUnionpay(activity);
	}

	@Override
	public void pay(int userId, int amount, String exParam) {
		if (spHelper.detectMobile_sp())
			new OrderUniopayInvoker(userId, amount).startJob();
	}

	class OrderUniopayInvoker extends Invoker {

		private int userId;
		private int amount;

		String xml;

		boolean ok = false;
		String msg = "";

		String orderId;

		public OrderUniopayInvoker(int userId, int amount) {
			this.userId = userId;
			this.amount = amount;
		}

		@Override
		protected void work() {
			try {
				JSONObject params = new JSONObject();
				params.put("userId", userId);
				params.put("targetId", userId); // targetId
				params.put("game", game);
				params.put("amount", amount);
				String json = HttpUtil.httpPost(Config.rechargeUrl
						+ "/charge/orderUnionPay", params);
				JSONObject rs = new JSONObject(json);
				if (rs.getInt("rs") != 0) {
					ok = false;
					msg = rs.getString("error");
				} else {
					ok = true;
					xml = rs.getString("content");
					orderId = rs.getString("orderId");
				}
			} catch (Exception e) {
				ok = false;
				msg = e.getMessage();
			}
		}

		@Override
		protected void onOK() {
			if (ok) {
				Intent intent = new Intent("UnionPayPlugin");
				Bundle xmlData = new Bundle();
				xmlData.putString("xml", xml);
				intent.putExtras(xmlData);
				activity.startActivity(intent);
			}
			onChargeSubmitListener.onSubmitOrder(orderId, ok, channel, msg);
		}

	}

}
