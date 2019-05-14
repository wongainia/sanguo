package com.vikings.sanguo.ui.window;

import java.io.IOException;
import java.lang.reflect.Method;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.alipay.AlipayDefine;
import com.alipay.BaseHelper;
import com.alipay.ResultChecker;
import com.alipay.SecurePayHelper;
import com.alipay.SecurePayer;
import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.RechargeState;
import com.vikings.sanguo.network.HttpConnector;
import com.vikings.sanguo.utils.CalcUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomPopupWindow;

public class AlipayWindow extends CustomPopupWindow implements
		OnEditorActionListener {
	private static final String tag = "AlipayWindow";
	private final static int MIN_INPUT_MONEY = 1;
	private final static int MAX_INPUT_MONEY = 100000;

	private ViewGroup amountLayout, msg1Layout, msg2Layout, msg3Layout,
			rechargeDescLayout;
	private EditText input;;

	private int rate;
	private SecurePayHelper spHelper;
	private BriefUserInfoClient user;

	private static int[] amounts = { 500, 1000, 2000, 5000, 10000 };
	private static int[] orders = { 1, 2, 3, 4, 5 };
	private static int custom_amount_order = 999; // 用户输入金额order

	public void open(RechargeState state, BriefUserInfoClient user) {
		if (null != state)
			this.rate = state.getRate();
		else
			this.rate = 0;
		this.user = user;
		doOpen();
	}

	@Override
	protected void init() {
		super.init("支付宝充值");
		setContent(R.layout.alipay_input);
		amountLayout = (ViewGroup) window.findViewById(R.id.amountLayout);
		msg1Layout = (ViewGroup) window.findViewById(R.id.msg1Layout);
		ViewUtil.setText(msg1Layout, R.id.gradientMsg, "选择充值金额");
		msg2Layout = (ViewGroup) window.findViewById(R.id.msg2Layout);
		ViewUtil.setText(msg2Layout, R.id.gradientMsg, "其他金额充值");
		msg3Layout = (ViewGroup) window.findViewById(R.id.msg3Layout);
		ViewUtil.setText(msg3Layout, R.id.gradientMsg, "充值说明");
		rechargeDescLayout = (ViewGroup) window
				.findViewById(R.id.rechargeDescLayout);
		input = (EditText) findViewById(R.id.input);
		input.setOnEditorActionListener(this);
		initAmountLayout();
		spHelper = new SecurePayHelper(Config.getController().getUIContext());

		WebView webView = ViewUtil.getWebView(controller.getUIContext());
		webView.loadUrl(CacheMgr.dictCache.getDict(Dict.TYPE_SITE_ADDR,
				(byte) 16));
		rechargeDescLayout.addView(webView);
		setBottomButton("确定", new OnClickListener() {

			@Override
			public void onClick(View v) {
				int money = getInputMoney();
				if (money < MIN_INPUT_MONEY || money > MAX_INPUT_MONEY) {
					controller.alert("金额无效,输入范围：1到100000元之间的整数");
					return;
				} else {
					doClick(custom_amount_order, money * Constants.CENT);
				}
			}
		});
	}

	private void initAmountLayout() {
		for (int i = 0; i < amounts.length; i++) {
			View view = controller.inflate(R.layout.recharge_input_item,
					amountLayout, false);
			amountLayout.addView(view);
			setView(view, orders[i], amounts[i]);
		}
	}

	private void setView(View view, final int order, final int amount) {
		ViewUtil.setText(view, R.id.desc, "充值 " + CalcUtil.addComma(amount)
				+ "元宝" + "（" + (amount / Constants.CENT) + "元）");
		if (rate > 0) {
			ViewUtil.setVisible(view, R.id.extDesc);
			ViewUtil.setText(view, R.id.extDesc,
					"额外赠送" + CalcUtil.addComma(amount * rate / 100) + "元宝");
		} else {
			ViewUtil.setGone(view, R.id.extDesc);
		}
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				doClick(order, amount);
			}
		});
	}

	public void doClick(int order, int amount) {
		if (-1 != order) {
			// 检测安全支付服务是否被安装
			if (spHelper.detectMobile_sp())
				new OrderAlipayInvoker(order, Integer.valueOf(Config
						.getGameId()), amount).start();
		}
	}

	private ProgressDialog mProgress = null;

	// 关闭进度框
	private void closeProgress() {
		try {
			if (mProgress != null) {
				mProgress.dismiss();
				mProgress = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class OrderAlipayInvoker extends BaseInvoker {
		private int order;
		private int gameId;
		private int amount;

		public OrderAlipayInvoker(int order, int gameId, int amount) {
			this.order = order;
			this.gameId = gameId;
			this.amount = amount;
		}

		@Override
		protected String failMsg() {
			return "获取失败";
		}

		@Override
		protected void fire() throws GameException {
			orderAlipay(order, gameId, amount); // Account.user.getId(),
		}

		@Override
		protected String loadingMsg() {
			return "请稍候...";
		}

		@Override
		protected void onOK() {

		}

		private void orderAlipay(int orderId, int gameId, int amount)
				throws GameException { // int targetId,
			String orderAlipayUrl = Config.rechargeUrl + "/charge/orderAlipay";

			String imei = "";
			String imsi = "";
			String phone = "";
			String mac = "";
			String ssid = "";
			int cellId = 0;
			int lac = 0;
			int mcc = 0;
			int mnc = 0;
			int clientVer;

			try {
				TelephonyManager tel = (TelephonyManager) Config
						.getController().getSystemService(
								Context.TELEPHONY_SERVICE);
				WifiManager wifi = (WifiManager) Config.getController()
						.getSystemService(Context.WIFI_SERVICE);
				clientVer = Config.getClientVer();
				try {
					imei = tel.getDeviceId();
				} catch (Exception e) {
				}
				try {
					imsi = tel.getSubscriberId();
				} catch (Exception e) {
				}
				try {
					phone = tel.getLine1Number();
				} catch (Exception e) {
				}
				if (wifi != null && wifi.isWifiEnabled()) {
					WifiInfo info = wifi.getConnectionInfo();
					mac = info.getBSSID();
					ssid = info.getSSID();
				}

				try {
					String str = tel.getNetworkOperator();
					if ((str == null)
							|| ((str.length() != 5) && (str.length() != 6)))
						str = tel.getSimOperator();
					str = str.trim();
					if (str.length() == 5 || str.length() == 6) {
						mcc = Integer.parseInt(str.substring(0, 3));
						mnc = Integer.parseInt(str.substring(3, str.length()));
					} else {
						mcc = 0;
						mnc = 0;
					}
				} catch (Exception e) {
				}

				try {
					int phoneType = tel.getPhoneType();
					CellLocation cellLocation = tel.getCellLocation();
					// GSM手机
					if (cellLocation != null) {
						if (phoneType == TelephonyManager.PHONE_TYPE_GSM
								&& cellLocation instanceof GsmCellLocation) {
							GsmCellLocation gl = (GsmCellLocation) cellLocation;
							cellId = gl.getCid();
							lac = gl.getLac();
						}
						// CDMA
						else if (phoneType == TelephonyManager.PHONE_TYPE_CDMA) {
							Class<?> clsCellLocation = cellLocation.getClass();
							Class<?>[] aryClass = new Class[0];
							try {
								Method localMethod1 = clsCellLocation
										.getMethod("getBaseStationId", aryClass);
								Method localMethod2 = clsCellLocation
										.getMethod("getSystemId", aryClass);
								Method localMethod3 = clsCellLocation
										.getMethod("getNetworkId", aryClass);
								Object[] aryDummy = new Object[0];
								int bid = ((Integer) localMethod1.invoke(
										cellLocation, aryDummy)).intValue();
								int sid = ((Integer) localMethod2.invoke(
										cellLocation, aryDummy)).intValue();
								int nid = ((Integer) localMethod3.invoke(
										cellLocation, aryDummy)).intValue();
								cellId = bid;
								lac = nid;
								mnc = sid;
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				} catch (Exception e) {
				}

				JSONObject params = new JSONObject();
				params.put("userId", Account.user.getId());
				params.put("targetId", user.getId()); // targetId
				params.put("orderId", orderId);
				params.put("game", Config.gameId);
				params.put("amount", amount);

				params.put("sid", Config.serverId);

				params.put("imsi", (null != imsi ? imsi : ""));
				params.put("imei", (null != imei ? imei : ""));
				params.put("phone", (null != phone ? phone : ""));
				params.put("mac", (null != mac ? mac : ""));
				params.put("ssid", (null != ssid ? ssid : ""));
				params.put("cellid", cellId);
				params.put("lac", lac);
				params.put("mcc", mcc);
				params.put("mnc", mnc);
				params.put("client_type", Config.clientCode); // (null !=
																// clientType ?
																// clientType :
																// "")
				params.put("client_ver", clientVer);

				String json = HttpConnector.getInstance().httpPost(
						orderAlipayUrl, params);
				JSONObject rs = new JSONObject(json);
				if (rs.getInt("rs") != 0)
					throw new GameException(rs.getString("error"));

				// 调用pay方法进行支付
				SecurePayer msp = new SecurePayer();
				boolean bRet = msp.pay(rs.getString("content"), mHandler,
						AlipayDefine.RQF_PAY, Config.getController()
								.getMainActivity());

				if (bRet) {
					closeProgress();
				}
			} catch (JSONException e) {
				Log.e(tag, e.getMessage(), e);
				throw new GameException("哎呀，出错了，稍后请重试!!");
			} catch (IOException e) {
				Log.e(tag, e.getMessage(), e);
				throw new GameException("网络异常,请重试");
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
							tradeStatus = strRet
									.substring(imemoStart, imemoEnd);

							// 对通知进行验签
							ResultChecker resultChecker = new ResultChecker(
									strRet);

							int retVal = resultChecker.checkSign();
							// 返回验签结果以及交易状态
							// 验签失败
							if (retVal == ResultChecker.RESULT_CHECK_SIGN_FAILED) {
								BaseHelper
										.showDialog(
												Config.getController()
														.getMainActivity(),
												"提示",
												Config.getController()
														.getUIContext()
														.getResources()
														.getString(
																R.string.check_sign_failed),
												android.R.drawable.ic_dialog_alert);
							} else {
								int code = Integer
										.valueOf(tradeStatus.substring(1,
												tradeStatus.length() - 1));
								if (9000 == code) {
									new RechargeResultTip(user, amount,
											RechargeState.ID_ALIPAY).show();

								} else {
									String str = CacheMgr.dictCache.getDict(
											Dict.TYPE_ALIPAY_ERR_CODE, code);
									controller.alert(str);
								}
							}

						} catch (Exception e) {
							e.printStackTrace();

							BaseHelper.showDialog(Config.getController()
									.getMainActivity(), "提示", strRet,
									R.drawable.alipay_infoicon);
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

	private int getInputMoney() {
		try {
			return Integer.valueOf(input.getText().toString());
		} catch (Exception e) {
		}
		return 0;
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		String str = input.getText().toString().trim();
		if (str.length() == 0 || !StringUtil.isNumeric(str))
			input.setText("1");
		else {
			int cnt = Integer.valueOf(str);
			if (cnt > MAX_INPUT_MONEY)
				input.setText("" + MAX_INPUT_MONEY);
			else if (cnt < MIN_INPUT_MONEY)
				input.setText("" + MIN_INPUT_MONEY);
			else
				input.setText("" + cnt);
		}

		return false;
	}
}
