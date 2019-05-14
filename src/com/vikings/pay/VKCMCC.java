package com.vikings.pay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.telephony.SmsManager;
import android.util.Log;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.utils.StringUtil;

public class VKCMCC extends VKPayService {

	private static final HashMap<Integer, String> code = new HashMap<Integer, String>();

	static {
		code.put(100, "000071393001");
		code.put(200, "000071393002");
		code.put(300, "000071393003");
		code.put(400, "000071393004");
		code.put(500, "000071393005");
		code.put(600, "000071393006");
		code.put(900, "000071393007");
		code.put(1000, "000071393008");
		// code.put(2000, "000071393009");
		// code.put(3000, "000071393010");
	}

	public static List<Integer> getAmounts() {
		List<Integer> amounts = new ArrayList<Integer>();
		amounts.addAll(code.keySet());
		Collections.sort(amounts);
		return amounts;
	}

	public VKCMCC(int game) {
		super(game);
	}

	@Override
	public void pay(int userId, int amount, String exParam) {
		if (!code.containsKey(amount)) {
			onChargeSubmitListener.onSubmitOrder("", false, channel, "不支持的购买项");
		} else {
			int maxAmount = CacheMgr.dictCache.getDictInt(
					Dict.TYPE_BATTLE_COST, 34);
			int thisMonthAmount = Config.getController().getFileAccess()
					.getRechargeAmountThisMonth("cmcc");
			if (thisMonthAmount >= maxAmount) {
				onChargeSubmitListener
						.onSubmitOrder(
								"",
								false,
								channel,
								"本月短信支付已达"
										+ (maxAmount / Constants.CENT)
										+ "元，请使用其他支付方式进行支付。<br/><br/>使用支付宝或网银支付还能享受充值额外返赠5%元宝优惠哦~");
				return;
			}
			new OrderInvoker(userId, amount, exParam).startJob();
		}
	}

	private class OrderInvoker extends Invoker {

		private int userId;
		private int amount;

		private int payUserId;

		private boolean ok;
		private String msg = "";

		private String orderId = "";

		private String sms;
		private String sendTo;

		public OrderInvoker(int userId, int amount, String exParam) {
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
				params.put("game", game);
				params.put("seq_no_3rd", code.get(amount));
				params.put("sid", Config.serverId);
				params.put("imsi", Config.getImsi());

				String json = HttpUtil.httpPost(Config.rechargeUrl
						+ "/charge/orderCMCCSMS", params);
				JSONObject rs = new JSONObject(json);
				if (rs.getInt("rs") != 0) {
					ok = false;
					msg = rs.getString("error");
				} else {
					ok = true;
					orderId = rs.getString("orderId");
					sms = rs.getString("msg");
					sendTo = rs.getString("sendTo");
				}
			} catch (Exception e) {
				ok = false;
				msg = "抱歉，充值失败！<br>本次操作未扣除费用，请重新尝试，或使用其他非短信类支付渠道更加稳定！";
				Log.e(VKCMCC.class.getSimpleName(), e.getMessage(), e);
			}
		}

		@Override
		void onOK() {
			if (ok) {
				try {
					SmsManager smsManager = SmsManager.getDefault();

					smsManager.sendTextMessage(sendTo, null, sms, null, null);
					PrepayReq.req(orderId);
					Config.getController().getFileAccess()
							.saveRechargeAmountThisMonth("cmcc", amount);
				} catch (Exception e) {
					Log.e("CMCCRechargeConfirmTip", e.getMessage(), e);
					ok = false;
					msg = "短信发送失败:";
				}
			}
			onChargeSubmitListener.onSubmitOrder(orderId, ok, channel, msg);
		}

	}

}
