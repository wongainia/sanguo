package com.vikings.pay;

import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alipay.AlipayDefine;
import com.alipay.BaseHelper;
import com.alipay.ResultChecker;
import com.alipay.SecurePayHelper;
import com.alipay.SecurePayer;
import com.vikings.sanguo.config.Config;

public class VKAlipay extends VKPayService {

	private SecurePayHelper spHelper;

	private Handler handler = new Handler();

	private Activity activity;

	private String error = "";

	private long orderId;

	private static HashMap<Integer, String> errorCode = new HashMap<Integer, String>();
	
	
	static{
		errorCode.put(9000	,"充值成功");
		errorCode.put(4000	,"您取消了充值操作（没有扣除您的费用)");
		errorCode.put(4001	,"订单数据异常，请重试（没有扣除您的费用)");
		errorCode.put(4003	,"您的支付宝账户已被冻结或不允许支付，请联系支付宝客服人员（没有扣除您的费用)");
		errorCode.put(4004	,"您的支付宝账户已解除绑定（没有扣除您的费用)");
		errorCode.put(4005	,"您的支付宝账户绑定失败或没有绑定（没有扣除您的费用)");
		errorCode.put(4006	,"订单支付失败，请重试（没有扣除您的费用)");
		errorCode.put(4010	,"您重新绑定了支付宝账户（没有扣除您的费用)");
		errorCode.put(6000	,"支付宝服务器正在升级（没有扣除您的费用)");
		errorCode.put(6001	,"您取消了充值操作（没有扣除您的费用)");
		errorCode.put(6002	,"网络连接异常，请重试（没有扣除您的费用)");

	}
	
	public static String getErrorCode(int code) {
		if(errorCode.containsKey(code))return errorCode.get(code);
		else
			return "支付宝错误码"+code;
	}
	
	public VKAlipay(int game,Activity activity) {
		super(game);
		this.activity = activity;
		spHelper = new SecurePayHelper(activity);
	}

	@Override
	public void pay(int userId, int amount, String exParam) {
		if (!spHelper.detectMobile_sp())
			return;
		new OrderAlipayInvoker(userId, amount).startJob();
	}

	private class OrderAlipayInvoker extends Invoker {

		private int userId;
		private int amount;

		public OrderAlipayInvoker(int userId, int amount) {
			this.amount = amount;
			this.userId = userId;
		}

		@Override
		void work() {
			try {
				JSONObject params = new JSONObject();
				params.put("userId", userId);
				params.put("targetId", userId); // targetId
				params.put("orderId", 999);
				params.put("game",game);
				params.put("amount", amount);
				String json = HttpUtil.httpPost(Config.rechargeUrl+"/charge/orderAlipay", params);
				JSONObject rs = new JSONObject(json);
				if (rs.getInt("rs") != 0) {
					error = rs.getString("error");
					handler.post(new Runnable() {
						@Override
						public void run() {
							onChargeSubmitListener.onSubmitOrder(
									String.valueOf(orderId), false,channel, error);
						}
					});
					return;
				}
				orderId = Long.valueOf(rs.getString("orderId"));
				// 调用pay方法进行支付
				SecurePayer msp = new SecurePayer();
				boolean bRet = msp.pay(rs.getString("content"), mHandler,
						AlipayDefine.RQF_PAY, activity);
				if (bRet) {
					closeProgress();
				}
			} catch (Exception e) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						onChargeSubmitListener.onSubmitOrder(
								String.valueOf(orderId), false,channel, "网络连接失败");
					}
				});
				Log.e(getClass().getSimpleName(), e.getMessage(),
						e);
			}
		}

		@Override
		void onOK() {
		}

	}

	private ProgressDialog mProgress = null;

	// 关闭进度框
	void closeProgress() {
		try {
			if (mProgress != null) {
				mProgress.dismiss();
				mProgress = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 这里接收支付结果，支付宝手机端同步通知
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			try {
				String strRet = (String) msg.obj;

				switch (msg.what) {
				case AlipayDefine.RQF_PAY: {
					//
					closeProgress();

					// 从通知中获取参数
					try {
						// 获取交易状态，具体状态代码请参看文档
						String tradeStatus = "resultStatus=";
						int imemoStart = strRet.indexOf("resultStatus=");
						imemoStart += tradeStatus.length();
						int imemoEnd = strRet.indexOf(";memo=");
						tradeStatus = strRet.substring(imemoStart, imemoEnd);

						// 对通知进行验签
						ResultChecker resultChecker = new ResultChecker(strRet);

						int retVal = resultChecker.checkSign();
						// 返回验签结果以及交易状态
						// 验签失败
						if (retVal == ResultChecker.RESULT_CHECK_SIGN_FAILED) {
							BaseHelper.showDialog(activity, "提示",
									"您的订单信息已被非法篡改",
									android.R.drawable.ic_dialog_alert);
						} else {
							int code = Integer.valueOf(tradeStatus.substring(1,
									tradeStatus.length() - 1));
							if (9000 == code) {
								onChargeSubmitListener.onSubmitOrder(
										String.valueOf(orderId) + "", true,channel,"");

							} else {
								onChargeSubmitListener.onSubmitOrder(
										String.valueOf(orderId), false,channel,
										getErrorCode(code));
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
						BaseHelper.showDialog(activity, "提示", strRet,
								android.R.drawable.ic_dialog_info);
					}
				}
					break;
				}

				super.handleMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

}
