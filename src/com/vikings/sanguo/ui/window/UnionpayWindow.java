package com.vikings.sanguo.ui.window;

import java.io.IOException;
import java.lang.reflect.Method;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
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

import com.alipay.SecurePayHelperUnionpay;
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

public class UnionpayWindow extends CustomPopupWindow implements
		OnEditorActionListener {
	private static final String tag = "UnionpayWindow";
	private final static int MIN_INPUT_MONEY = 1;
	private final static int MAX_INPUT_MONEY = 100000;

	private ViewGroup amountLayout, msg1Layout, msg2Layout, msg3Layout,
			rechargeDescLayout;
	private EditText input;;

	private int rate;

	private static int[] amounts = { 500, 1000, 2000, 5000, 10000 };

	private BriefUserInfoClient user;

	private boolean isCreditCard = false;

	private SecurePayHelperUnionpay spHelper = new SecurePayHelperUnionpay(
			Config.getController().getUIContext());

	public void open(RechargeState state, BriefUserInfoClient user,
			boolean isCreditCard) {
		if (state != null)
			this.rate = state.getRate();
		else
			this.rate = 0;
		this.user = user;
		this.isCreditCard = isCreditCard;
		doOpen();
	}

	@Override
	protected void init() {
		super.init("银联充值");
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

		WebView webView = ViewUtil.getWebView(controller.getUIContext());
		webView.loadUrl(CacheMgr.dictCache.getDict(Dict.TYPE_SITE_ADDR,
				(byte) 27));
		rechargeDescLayout.addView(webView);
		setBottomButton("确定", new OnClickListener() {

			@Override
			public void onClick(View v) {
				int money = getInputMoney();
				if (money < MIN_INPUT_MONEY || money > MAX_INPUT_MONEY) {
					controller.alert("金额无效,输入范围：1到100000元之间的整数");
					return;
				} else {
					doClick(money * Constants.CENT);
				}
			}
		});
	}

	private void initAmountLayout() {
		for (int i = 0; i < amounts.length; i++) {
			View view = controller.inflate(R.layout.recharge_input_item,
					amountLayout, false);
			amountLayout.addView(view);
			setView(view, amounts[i]);
		}
	}

	private void setView(View view, final int amount) {
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
				doClick(amount);
			}
		});
	}

	public void doClick(int amount) {
		if (spHelper.detectMobile_sp())
			new OrderUniopayInvoker(Integer.valueOf(Config.getGameId()), amount)
					.start();
	}

	class OrderUniopayInvoker extends BaseInvoker {
		private int gameId;
		private int amount;

		String xml;

		public OrderUniopayInvoker(int gameId, int amount) {
			this.gameId = gameId;
			this.amount = amount;
		}

		@Override
		protected String failMsg() {
			return "获取失败";
		}

		@Override
		protected void fire() throws GameException {
			orderUnionPay(gameId, amount); // Account.user.getId(),
		}

		@Override
		protected String loadingMsg() {
			return "请稍候...";
		}

		@Override
		protected void onOK() {
			Intent intent = new Intent("UnionPayPlugin");
			Bundle xmlData = new Bundle();
			xmlData.putString("xml", xml);
			// 下面参数，传“0”代表一般充值通道；传“1”代表单独信用卡通道。
			xmlData.putString("isCreditCard", isCreditCard ? "1" : "0");
			intent.putExtras(xmlData);
			Config.getController().getMainActivity().startActivity(intent);
		}

		private void orderUnionPay(int gameId, int amount) throws GameException { // int
																					// targetId,
			String orderUnionPayUrl = Config.rechargeUrl
					+ "/charge/orderUnionPay";

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
			// String clientType;

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

				// clientType = Config.clientCode;

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
						orderUnionPayUrl, params);
				JSONObject rs = new JSONObject(json);
				if (rs.getInt("rs") != 0)
					throw new GameException(rs.getString("error"));
				xml = rs.getString("content");
			} catch (JSONException e) {
				Log.e(tag, e.getMessage(), e);
				throw new GameException("哎呀，出错了，稍后请重试!!");
			} catch (IOException e) {
				Log.e(tag, e.getMessage(), e);
				throw new GameException("网络异常,请重试");
			}
		}

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
		int cnt = StringUtil.parseInt(str);
		if (cnt < MIN_INPUT_MONEY || cnt > MAX_INPUT_MONEY)
			cnt = MIN_INPUT_MONEY;
		input.setText("" + cnt);
		return false;
	}
}