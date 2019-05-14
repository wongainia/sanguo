package com.vikings.pay;

import java.util.HashMap;

import mm.purchasesdk.OnPurchaseListener;
import mm.purchasesdk.Purchase;
import mm.purchasesdk.PurchaseCode;

import org.json.JSONObject;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.utils.StringUtil;

public class VKCMCCMM extends VKPayService {

	private Purchase purchase;

	public final static int[] amount = { 1, 100, 200, 300, 400, 500, 600, 800,
			900, 1000, 1500, 2000, 3000 };

	private final static String[] code = { "30000763457213", "30000763457201",
			"30000763457202", "30000763457211", "30000763457203",
			"30000763457204", "30000763457205", "30000763457206",
			"30000763457212", "30000763457207", "30000763457208",
			"30000763457209", "30000763457210" };

	public static int[] getAmount() {
		if (Config.isMMPak()) {
			return amount;
		} else {
			int[] newAmount = new int[amount.length - 1];
			for (int i = 0; i < newAmount.length; i++) {
				newAmount[i] = amount[i + 1];
			}
			return newAmount;
		}

	}

	public static int getAmount(int a) {
		for (int i = 0; i < amount.length; i++) {
			if (amount[i] >= a)
				return amount[i];
		}
		return amount[amount.length - 1];
	}

	private static String getPayCode(int a) {
		for (int i = 0; i < amount.length; i++) {
			if (amount[i] == a)
				return code[i];
		}
		return code[0];
	}

	private String orderId = "";

	private int payAmount = 0;

	private String payCode = "";

	public VKCMCCMM(int game) {
		super(game);
		initSDK();
	}

	public void initSDK() {
		purchase = Purchase.getInstance();
		try {
			purchase.setAppInfo("300007634572", "C9916F9A10756C6F");
			purchase.init(Config.getController().getUIContext(), opl);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private OnPurchaseListener opl = new OnPurchaseListener() {

		@Override
		public void onInitFinish(int arg0) {
		}

		@Override
		public void onBeforeDownload() {
		}

		@Override
		public void onBeforeApply() {
		}

		@Override
		public void onAfterDownload() {
		}

		@Override
		public void onAfterApply() {
		}

		@Override
		public void onBillingFinish(int code, HashMap arg1) {
			String result = "订购成功";
			// 此次订购的orderID
			String orderCmcc = null;
			// // 商品的paycode
			// String paycode = null;
			// // 商品的有效期(仅租赁类型商品有效)
			// String leftday = null;
			// // 商品的交易 ID，用户可以根据这个交易ID，查询商品是否已经交易
			// String tradeID = null;
			boolean ok = false;
			if (code == PurchaseCode.ORDER_OK || (code == PurchaseCode.AUTH_OK)) {
				/**
				 * 商品购买成功或者已经购买。 此时会返回商品的paycode，orderID,以及剩余时间(租赁类型商品)
				 */
				ok = true;
				if (arg1 != null) {
					orderCmcc = (String) arg1.get(OnPurchaseListener.ORDERID);
					if (orderCmcc != null && orderCmcc.trim().length() != 0) {
						result = result + "订单号： " + orderCmcc;
					}
					// paycode = (String) arg1.get(OnPurchaseListener.PAYCODE);
					// if (paycode != null && paycode.trim().length() != 0) {
					// result = result + ",Paycode:" + paycode;
					// }
					// tradeID = (String) arg1.get(OnPurchaseListener.TRADEID);
					// if (tradeID != null && tradeID.trim().length() != 0) {
					// result = result + ",tradeID:" + tradeID;
					// }
				}
				new QueryInvoker(orderId, orderCmcc).startJob();
				// PrepayReq.req(orderId);
				// Config.getController().getFileAccess()
				// .saveRechargeAmountThisMonth("zsy_mm", payAmount);
			} else {
				result = "订购结果：" + Purchase.getReason(code);
			}
			onChargeSubmitListener.onSubmitOrder(orderId, ok, channel, result);
		}

		@Override
		public void onQueryFinish(int code, HashMap arg1) {
		}

		@Override
		public void onUnsubscribeFinish(int arg0) {

		}

	};

	@Override
	public void pay(int userId, int a, String exParam) {
		payAmount = getAmount(a);
		// int maxAmount = CacheMgr.dictCache
		// .getDictInt(Dict.TYPE_BATTLE_COST, 37);
		// int thisMonthAmount = Config.getController().getFileAccess()
		// .getRechargeAmountThisMonth("zsy_mm");
		// if (thisMonthAmount >= maxAmount) {
		// onChargeSubmitListener.onSubmitOrder("", false, channel, "本月短信支付已达"
		// + (maxAmount / Constants.CENT)
		// + "元，请使用其他支付方式进行支付。<br/><br/>使用支付宝或网银支付还能享受充值额外返赠5%元宝优惠哦~");
		// return;
		// }
		new PayInvoker(userId, getAmount(a), exParam).startJob();
	}

	private class PayInvoker extends Invoker {

		private int payUserId;

		private int userId;

		private int amount;

		private String msg;

		private boolean ok;

		private String code;

		public PayInvoker(int userId, int amount, String exParam) {
			this.amount = amount;
			this.userId = userId;
			payUserId = StringUtil.parseInt(exParam);
		}

		@Override
		void work() {
			try {
				JSONObject params = new JSONObject();
				params.put("userId", payUserId == 0 ? userId : payUserId);
				params.put("targetId", userId);
				params.put("game", Config.gameId);
				params.put("amount", amount);
				params.put("sid", Config.serverId);
				params.put("imsi", Config.getImsi());
				params.put("channel", channel);

				// String json = HttpUtil.httpPost(Config.rechargeUrl
				// + "/charge/orderCMCCMMSMS", params);

				String json = HttpUtil.httpPost(Config.rechargeUrl
						+ "/charge/orderCommon", params);
				JSONObject rs = new JSONObject(json);
				if (rs.getInt("rs") != 0) {
					ok = false;
					msg = rs.getString("error");
					return;
				}
				orderId = rs.getString("content");
				code = getPayCode(amount);
				ok = true;
			} catch (Exception e) {
				ok = false;
				msg = "抱歉，充值失败！<br>本次操作未扣除费用，请重新尝试，或使用其他非短信类支付渠道更加稳定！";
			}
		}

		@Override
		void onOK() {
			if (ok) {
				Config.getController().dismissLoading();
				purchase.order(Config.getController().getUIContext(), code, opl);
			} else
				onChargeSubmitListener.onSubmitOrder("", false, channel, msg);
		}

	}

	private class QueryInvoker extends Invoker {

		private String orderId;

		private String cmccOrderId;

		public QueryInvoker(String orderId, String cmccOrderId) {
			this.cmccOrderId = cmccOrderId;
			this.orderId = orderId;
		}

		@Override
		void work() {
			try {
				JSONObject params = new JSONObject();
				params.put("orderId", orderId);
				params.put("cmccOrderId", cmccOrderId);
				String json = HttpUtil.httpPost(Config.rechargeUrl
						+ "/charge/checkMM7K", params);
			} catch (Exception e) {
			}
		}

		@Override
		void onOK() {
		}

	}

}
