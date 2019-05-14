package com.vikings.pay;

import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import com.vikings.sanguo.config.Config;

public class VKPayMgr {

	private int gameId;

	private Activity activity;

	private Handler handler;

	private HashMap<Integer, VKPayService> payServices = new HashMap<Integer, VKPayService>();

	public VKPayMgr(Activity activity, int gameId) {
		this.activity = activity;
		this.gameId = gameId;
		this.handler = new Handler();
		initPayServices();
	}

	public boolean containsMM() {
		return containsPayChanel(VKConstants.CHANNEL_CMCC_MM);
	}

	private boolean containsPayChanel(int chanel) {
		return payServices.containsKey(chanel);
	}

	private void initPayServices() {
		// payServices.put(VKConstants.CHANNEL_CMCC_MM, new VKCMCCMM(gameId));

		payServices.put(VKConstants.CHANNEL_ALIPAY, new VKAlipay(gameId,
				activity));
		// payServices.put(VKConstants.CHANNEL_CMCC, new VKCMCC(gameId));
		payServices.put(VKConstants.CHANNEL_UNIONPAY, new VKUnionpay(gameId,
				activity));
		payServices.put(VKConstants.CHANNEL_TELCOM, new VKTelcom(gameId));

		payServices.put(VKConstants.CHANNEL_SKYPAY, new VKSkyPay(gameId,
				activity));
	}

	public void pay(final int channel, final int userId, final int amount,
			final String exParam,
			final OnChargeSubmitListener onChargeSubmitListener) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				if (!payServices.containsKey(channel)) {
					onChargeSubmitListener.onSubmitOrder("", false, channel,
							"不支持的购买渠道");
					return;
				}
				payServices.get(channel).setChannel(channel)
						.setOnChargeSubmitListener(onChargeSubmitListener)
						.pay(userId, amount, exParam);
			}
		});
	}

	public void query(final String orderId,
			final OnChargeQueryListener onChargeQueryListener) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				new QueryInvoker(Long.valueOf(orderId), onChargeQueryListener)
						.startJob();
			}

		});
	}

	private class QueryInvoker extends Invoker {

		private long id;

		private boolean ok = false;

		private String msg = "";

		private OnChargeQueryListener onChargeQueryListener;

		public QueryInvoker(long id, OnChargeQueryListener onChargeQueryListener) {
			this.id = id;
			this.onChargeQueryListener = onChargeQueryListener;
		}

		@Override
		void work() {
			try {
				JSONObject params = new JSONObject();
				params.put("orderId", id);
				String json = HttpUtil.httpPost(Config.rechargeUrl
						+ "/charge/checkOrder", params);
				JSONObject rs = new JSONObject(json);
				if (rs.getInt("rs") != 0) {
					msg = rs.getString("error");
					return;
				}
				int status = rs.getInt("content");
				if (status == 2) {
					ok = true;
				} else if (status == 1) {
					msg = "订单支付结果未通知";
				} else {
					msg = "订单不存在";
				}
			} catch (Exception e) {
				msg = e.getMessage();
				Log.e(VKPayMgr.class.getSimpleName(), e.getMessage(), e);
			}
		}

		@Override
		void onOK() {
			onChargeQueryListener.onQueryResult(String.valueOf(id), ok, msg);
		}

	}

}
