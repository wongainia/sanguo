package com.vikings.pay;

import org.json.JSONObject;

import com.egame.webfee.EgameFee;
import com.egame.webfee.EgameFeeChannel;
import com.egame.webfee.EgameFeeResultListener;
import com.vikings.sanguo.Constants;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.utils.StringUtil;

public class VKTelcom extends VKPayService {
	public final static int[] amount = { 100, 500, 1500 };

	public static int getAmount(int a) {
		for (int i = 0; i < amount.length; i++) {
			if (amount[i] >= a)
				return amount[i];
		}
		return amount[amount.length - 1];
	}

	public VKTelcom(int game) {
		super(game);
		initEGameSDK();
	}

	public static void initEGameSDK() {
		EgameFee.init(Config.getController().getMainActivity(),
				new EgameFeeResultListener() {

					public void egameFeeSucceed(int gameUserId, int feeMoney,
							EgameFeeChannel feeChannel) {
						Config.getController().alert("充值成功");
						PrepayReq.req(orderId);
						Config.getController()
								.getFileAccess()
								.saveRechargeAmountThisMonth("telecom",
										feeMoney * Constants.CENT);
					}

					public void egameFeeCancel() {
						Config.getController().alert("您已取消充值");
					}

					public void egameFeeFailed() {
						Config.getController().alert("充值失败");
					}

					@Override
					public void egameFeeTimeout() {
					}
				});
	}

	@Override
	public void pay(int userId, int a, String exParam) {
		int maxAmount = CacheMgr.dictCache
				.getDictInt(Dict.TYPE_BATTLE_COST, 34);
		int thisMonthAmount = Config.getController().getFileAccess()
				.getRechargeAmountThisMonth("telecom");
		if (thisMonthAmount >= maxAmount) {
			onChargeSubmitListener.onSubmitOrder("", false, channel, "本月短信支付已达"
					+ (maxAmount / Constants.CENT)
					+ "元，请使用其他支付方式进行支付。<br/><br/>使用支付宝或网银支付还能享受充值额外返赠5%元宝优惠哦~");
			return;
		}
		new PayInvoker(userId, getAmount(a), exParam).startJob();
	}

	private static String orderId = "0";

	private class PayInvoker extends Invoker {

		private int payUserId;

		private int userId;

		private int amount;

		private String msg;

		private boolean ok;

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

				String json = HttpUtil.httpPost(Config.rechargeUrl
						+ "/charge/order189", params);
				JSONObject rs = new JSONObject(json);
				if (rs.getInt("rs") != 0) {
					ok = false;
					msg = rs.getString("error");
					return;
				}
				userId = rs.getInt("content");
				ok = true;
			} catch (Exception e) {
				ok = false;
				msg = "抱歉，充值失败！<br>本次操作未扣除费用，请重新尝试，或使用其他非短信类支付渠道更加稳定！";
			}
		}

		@Override
		void onOK() {
			if (ok) {
				EgameFee.payBySms(userId, amount / 100, true);
				orderId = userId + "";
			}
			onChargeSubmitListener.onSubmitOrder(userId + "", ok, channel, msg);
		}

	}

}
