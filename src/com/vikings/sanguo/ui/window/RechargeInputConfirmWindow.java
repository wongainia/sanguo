package com.vikings.sanguo.ui.window;

import java.io.IOException;
import java.lang.reflect.Method;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.RechargeCardInfo;
import com.vikings.sanguo.network.YeePayConnector;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.ui.alert.TouchCloseConfirm;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomPopupWindow;

public class RechargeInputConfirmWindow extends CustomPopupWindow implements
		OnClickListener {
	private final static String tag = "RechargeInputConfirmWindow";
	private RechargeCardInfo cardInfo;
	private View msg1Layout, msg2Layout, msg3Layout, msg4Layout, confirm,
			cancel;

	public void open(RechargeCardInfo cardInfo) {
		this.cardInfo = cardInfo;
		doOpen();
	}

	@Override
	protected void init() {
		super.init("充值确认");
		setContent(R.layout.recharge_input_confirm);
		msg1Layout = window.findViewById(R.id.msg1Layout);
		msg2Layout = window.findViewById(R.id.msg2Layout);
		msg3Layout = window.findViewById(R.id.msg3Layout);
		msg4Layout = window.findViewById(R.id.msg4Layout);
		setTextTitle(msg1Layout, "充值卡种类：");
		setTextTitle(msg2Layout, "充值卡面额：");
		setTextTitle(msg3Layout, "充值卡序列号：");
		setTextTitle(msg4Layout, "充值卡密码：");
		ViewUtil.setText(window, R.id.cardType, cardInfo.getCardChannelDesc());
		ViewUtil.setText(window, R.id.cardAmount, cardInfo.getAmount() + "元");
		ViewUtil.setText(window, R.id.serial, cardInfo.getSerial());
		ViewUtil.setText(window, R.id.pswd, cardInfo.getPswd());
		ViewUtil.setText(window, R.id.desc, Html.fromHtml(CacheMgr.dictCache
				.getDict(Dict.TYPE_RECHARGE_DIRECT, 4)));

		confirm = bindButton(window, R.id.confirm, this);
		cancel = bindButton(window, R.id.cancel, this);
	}

	public void setTextTitle(View view, String txt) {
		TextView gradientMsg = (TextView) view.findViewById(R.id.gradientMsg);
		LayoutParams params = (LayoutParams) gradientMsg.getLayoutParams();
		params.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
		params.leftMargin = (int) (30 * Config.SCALE_FROM_HIGH);
		gradientMsg.setLayoutParams(params);
		gradientMsg.setTextSize(14);
		ViewUtil.setText(gradientMsg, txt);
	}

	@Override
	public void onClick(View v) {
		if (v == confirm) {
			new YeePayInvoker().start();
		} else if (v == cancel)
			controller.goBack();
	}

	class YeePayInvoker extends BaseInvoker {
		private int r1Code;
		private String cardStatus;

		@Override
		protected String failMsg() {
			return "失败了...";
		}

		@Override
		protected void fire() throws GameException {
			orderYeePay();
		}

		@Override
		protected String loadingMsg() {
			return "请稍候...";
		}

		@Override
		protected void onOK() {
			if (1 == r1Code) {
				SoundMgr.play(R.raw.sfx_tips2);
				new RechargeResultTip(cardInfo.getTarget(),
						cardInfo.getAmount() * Constants.CENT,
						cardInfo.getChannel()).show();
			} else {

				final TouchCloseConfirm confirm = new TouchCloseConfirm();

				OnClickListener l = new OnClickListener() {
					@Override
					public void onClick(View v) {
						confirm.dismiss();
						controller.goBack();
					}
				};

				String content = "";
				if (-1 == r1Code) {
					content = "您当前的网络状况造成订单成功支付的延迟，请稍后检查您的账户。如仍未到账，请联系客服人员!";
					confirm.show(content, "订单已提交", l);
					return;
				}

				content = CacheMgr.dictCache.getDict(Dict.TYPE_YEEPAY_ERR_CODE,
						Integer.valueOf(cardStatus));
				if (StringUtil.isNull(content))
					content = "您的充值卡提交失败" + "<br/>" + "请核对序列号和密码后重新输入";
				confirm.show(content, "支付失败", l);
			}
		}

		private void orderYeePay() throws GameException {
			String orderYeePayUrl = Config.rechargeUrl + "/charge/orderYeepay";

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

			TelephonyManager tel = (TelephonyManager) Config.getController()
					.getSystemService(Context.TELEPHONY_SERVICE);
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

			// clientType = Build.MODEL;

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
							Method localMethod1 = clsCellLocation.getMethod(
									"getBaseStationId", aryClass);
							Method localMethod2 = clsCellLocation.getMethod(
									"getSystemId", aryClass);
							Method localMethod3 = clsCellLocation.getMethod(
									"getNetworkId", aryClass);
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

			try {
				JSONObject params = new JSONObject();
				params.put("userId", Account.user.getId());
				params.put("targetId", cardInfo.getTarget().getId());
				params.put("game", Config.gameId);
				params.put("amount", cardInfo.getAmount() * Constants.CENT); // cardInfo.getAmount()
				params.put("cardNo", cardInfo.getSerial());
				params.put("cardPwd", cardInfo.getPswd());
				params.put("cardChannel", cardInfo.getChannelStr());
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

				String json = YeePayConnector.getInstance().httpPost(
						orderYeePayUrl, params);
				JSONObject rs = new JSONObject(json);
				if (rs.getInt("rs") != 0)
					throw new GameException(rs.getString("error"));

				if (rs.isNull("r1_Code"))
					r1Code = -1;
				else {
					r1Code = rs.getInt("r1_Code");
					cardStatus = rs.getString("p8_cardStatus");
				}

			} catch (JSONException e) {
				Log.e(tag, e.getMessage(), e);
				throw new GameException("HTTP参数错误!");
			} catch (IOException e) {
				Log.e(tag, e.getMessage(), e);
				throw new GameException("网络异常,请重试");
			}
		}
	}

}
