package com.vikings.pay;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.skymobi.pay.sdk.SkyPayServer;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.security.DeviceInfo;
import com.vikings.sanguo.utils.StringUtil;

public class VKSkyPay extends VKPayService {

	private static Activity activity;

	private static SkyPayServer skyPayServer;

	private boolean isInit = false;

	private static boolean fetch = false;// 是否取到价格

	private String orderId = "";

	private boolean ok;
	private String msg = "";

	private String skyPayAppId = "7002098";
	private String appName = "后宫三国";

	private static int[] amount = { 100, 200, 300, 400, 500, 600, 700, 800,
			900, 1000 };

	private static int[] defaultAmount = { 200, 400, 600, 800, 1000 };

	public static int[] getAmount() {
		if (fetch)
			return amount;
		else
			return defaultAmount;

	}

	public VKSkyPay(int game, Activity activity) {
		super(game);
		this.activity = activity;
		initSDK();
	}

	private void initSDK() {
		skyPayServer = SkyPayServer.getInstance();
		PayCallBackHandle payCallBackHandle = new PayCallBackHandle();
		int initRet = skyPayServer.init(payCallBackHandle);
		if (SkyPayServer.PAY_RETURN_SUCCESS == initRet) {
			isInit = true;
			priceInit(Account.user.getId());// 初始化价格
		} else {
			isInit = false;
			Log.i("sky pay", "初始化失败:" + initRet);
		}
	}

	public static boolean isValidAmount(int amount) {
		return contains(amount);
	}

	private static boolean contains(int a) {
		for (int i = 0; i < amount.length; i++) {
			if (amount[i] == a)
				return true;
		}
		return false;
	}

	private int indexOf(int a) {
		for (int i = 0; i < amount.length; i++) {
			if (amount[i] == a)
				return i + 1;
		}
		return -1;
	}

	private int getVerCode() {
		try {
			return activity.getPackageManager().getPackageInfo(
					activity.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			return 0;
		}
	}

	public int priceInit(int userId) {
		// 开始预取价格
		String merchantId = "13664";
		String paymethod = "sms";
		String appVersion = "" + getVerCode();
		String systemId = "300024";
		String channelId = "daiji_vk_" + Config.getChannel();
		String payType = "3";
		String account = "" + userId;

		String mOrderInfo = "merchantId=" + merchantId + "&" + "appId="
				+ skyPayAppId + "&" + "payMethod=" + paymethod + "&"
				+ "appName=" + appName + "&" + "appVersion=" + appVersion + "&"
				+ "systemId=" + systemId + "&" + "channelId=" + channelId + "&"
				+ "payType=" + payType + "&" + "appUserAccount=" + account;
		int payRet = skyPayServer.prefetchPriceInit(activity, mOrderInfo);
		String priceStr = getPriceList();
		if (!StringUtil.isNull(priceStr)) {
			fetch = true;
			amount = StringUtil.parseIntArr(priceStr);
		}
		return payRet;
	}

	public static String getPriceList() {
		String ret = skyPayServer.prefetchPriceGet(activity);
		if (ret == null) {
			ret = "";
		}
		return ret;
	}

	@Override
	public void pay(int userId, int amount, String exParam) {
		if (amount == -1)// 初始化价格
		{
			return;
		} else if (amount == 0)// 获取价格列表
		{
			String ret = skyPayServer.prefetchPriceGet(activity);
			if (ret == null) {
				onChargeSubmitListener.onSubmitOrder("fetchPrice", false,
						channel, "");
			} else {
				ret = ret.replace(',', '|');
				onChargeSubmitListener.onSubmitOrder("fetchPrice", true,
						channel, ret);
			}
			return;

		}
		if (!contains(amount)) {
			onChargeSubmitListener.onSubmitOrder("", false, channel, "不支持的购买项");
			return;
		}
		if (!isInit) {
			onChargeSubmitListener.onSubmitOrder("", false, channel,
					"支付插件初始化失败，请重启游戏重试");
			return;
		}
		new OrderInvoker(userId, amount, exParam).startJob();
	}

	private class OrderInvoker extends Invoker {

		private int userId;
		private int amount;

		private int payUserId;

		private String content = "";

		public OrderInvoker(int userId, int amount, String exParam) {
			this.amount = amount;
			this.userId = userId;
			payUserId = StringUtil.parseInt(exParam);
		}

		@Override
		void work() {
			try {
				JSONObject params = DeviceInfo.getInstance().getJSONObject();
				params.put("userId", payUserId == 0 ? userId : payUserId);
				params.put("targetId", userId);
				params.put("game", game);
				params.put("amount", amount);
				params.put("sid", Config.serverId);
				params.put("channel", channel);
				params.put("appId", skyPayAppId);
				params.put("appName", appName);
				params.put("payPointNum", indexOf(amount));
				params.put("appVersion", getVerCode());
				String json = HttpUtil.httpPost(Config.rechargeUrl
						+ "/charge/orderSkyapy", params);
				JSONObject rs = new JSONObject(json);
				if (rs.getInt("rs") != 0) {
					ok = false;
					msg = rs.getString("error");
					return;
				}
				orderId = rs.getString("orderId");
				content = rs.getString("content");
				content = content + "&channelId=daiji_vk_"
						+ Config.getChannel();
				ok = true;
			} catch (Exception e) {
				ok = false;
				msg = "抱歉，充值失败！<br>本次操作未扣除费用，请重新尝试，或使用其他非短信类支付渠道更加稳定！";
			}
		}

		@Override
		void onOK() {
			if (ok) {
				skyPayServer.startActivityAndPay(activity, content);
			} else {
				onChargeSubmitListener.onSubmitOrder(orderId, ok, channel, msg);
			}
		}
	}

	private class PayCallBackHandle extends Handler {

		public static final String STRING_MSG_CODE = "msg_code";
		public static final String STRING_PAY_STATUS = "pay_status";
		public static final String STRING_CHARGE_STATUS = "3rdpay_status";

		@Override
		public void handleMessage(Message m) {
			super.handleMessage(m);

			if (m.what == SkyPayServer.MSG_WHAT_TO_APP) {
				msg = "";
				ok = false;
				// 形式：key-value
				String retInfo = (String) m.obj;
				Map<String, String> map = new HashMap<String, String>();
				String[] keyValues = retInfo.split("&|=");
				for (int i = 0; i < keyValues.length; i = i + 2) {
					map.put(keyValues[i], keyValues[i + 1]);
				}

				int msgCode = Integer.parseInt(map.get(STRING_MSG_CODE));
				if (msgCode == 101) {
					/**
					 * 返回错误 retInfo格式：msg_code=101&error_code=***
					 * error_code取值参考文档
					 */
					// msg = map.get("error_code");
					ok = false;
				} else {
					if (map.get(STRING_PAY_STATUS) != null) {
						/**
						 * 短信付费结果返回
						 * retInfo失败格式：msg_code=100&pay_status=101&pay_price
						 * =0&errorcode=***
						 * retInfo成功格式：msg_code=100&pay_status=102&pay_price=***
						 * 具体参数意义参考文档
						 */

						if ("102".equals(map.get(STRING_PAY_STATUS))) {
							ok = true;
						} else {
							ok = false;
							// msg = map.get("errorcode");
						}
					} else if (map.get(STRING_CHARGE_STATUS) != null) {
						/**
						 * 第三方付费结果返回
						 * retInfo格式：msg_code=100&3rdpay_status=***&pay_price
						 * =***&skyChargeId=*** 具体参数意义参考文档
						 */
						ok = false;
						msg = "不支持第三方支付";
					}
				}
				onChargeSubmitListener.onSubmitOrder(orderId, ok, channel, msg);
			}
		}
	}

}
