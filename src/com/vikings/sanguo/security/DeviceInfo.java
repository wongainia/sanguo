package com.vikings.sanguo.security;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

import com.vikings.sanguo.config.Config;

//初始化设备信息
public class DeviceInfo {
	private static DeviceInfo info;

	private String imei = "";

	private String imsi = "";

	private String iccid = "";

	private String net = "";

	private String phone = "";

	private String mac = "";
	private String ssid = "";

	private String ip = "";

	private int cellId = 0;
	private int lac = 0;
	private int mcc = 0;
	private int mnc = 0;

	private String phoneType = "";
	private long memery = 0;
	private int screenWidth = 0;
	private int screenHeight = 0;
	private String os = "";

	private DeviceInfo() {
		initValue();
	}

	public static DeviceInfo getInstance() {
		if (null == info)
			info = new DeviceInfo();
		info.initValue();
		return info;
	}

	public String getImsi() {
		return imsi;
	}

	private void initValue() {
		TelephonyManager tel = (TelephonyManager) Config.getController()
				.getSystemService(Context.TELEPHONY_SERVICE);
		WifiManager wifi = (WifiManager) Config.getController()
				.getSystemService(Context.WIFI_SERVICE);
		ActivityManager activityManager = (ActivityManager) Config
				.getController().getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(memoryInfo);
		memery = memoryInfo.availMem;
		try {
			imei = tel.getDeviceId();
		} catch (Exception e) {
		}
		try {
			imsi = tel.getSubscriberId();
		} catch (Exception e) {
		}

		try {
			ip = getIPAddress();
		} catch (Exception e) {
		}

		try {
			net = getNetTypeName(tel.getNetworkType());
		} catch (Exception e) {
		}

		try {
			iccid = tel.getSimSerialNumber();
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
		phoneType = Build.MODEL;
		os = Build.VERSION.RELEASE;
		screenHeight = Config.screenHeight;
		screenWidth = Config.screenWidth;
		try {
			String str = tel.getNetworkOperator();
			if ((str == null) || ((str.length() != 5) && (str.length() != 6)))
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
					// gsm = true;
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
						int bid = ((Integer) localMethod1.invoke(cellLocation,
								aryDummy)).intValue();
						int sid = ((Integer) localMethod2.invoke(cellLocation,
								aryDummy)).intValue();
						int nid = ((Integer) localMethod3.invoke(cellLocation,
								aryDummy)).intValue();
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
	}

	public JSONObject getJSONObject() {
		JSONObject jo = new JSONObject();
		try {
			jo.put("imsi", imsi);
			jo.put("imei", imei);
			jo.put("iccid", iccid);
			jo.put("ip", ip);
			jo.put("net", net);
			jo.put("phone", phone);
			jo.put("mac", mac);
			jo.put("ssid", ssid);
			jo.put("cellId", cellId);
			jo.put("lac", lac);
			jo.put("mcc", mcc);
			jo.put("mnc", mnc);
			jo.put("phoneType", phoneType);
			jo.put("memery", memery);
			jo.put("screenW", screenWidth);
			jo.put("screenH", screenHeight);
			jo.put("os", os);
		} catch (JSONException e) {
		}
		return jo;
	}

	private String getIPAddress() {
		try {
			final Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface
					.getNetworkInterfaces();

			while (networkInterfaceEnumeration.hasMoreElements()) {
				final NetworkInterface networkInterface = networkInterfaceEnumeration
						.nextElement();

				final Enumeration<InetAddress> inetAddressEnumeration = networkInterface
						.getInetAddresses();

				while (inetAddressEnumeration.hasMoreElements()) {
					final InetAddress inetAddress = inetAddressEnumeration
							.nextElement();

					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress();
					}
				}
			}

			return "";
		} catch (final SocketException e) {
			return "";
		}
	}

	private String getNetTypeName(final int pNetType) {
		switch (pNetType) {
		case 0:
			return "unknown";
		case 1:
			return "GPRS";
		case 2:
			return "EDGE";
		case 3:
			return "UMTS";
		case 4:
			return "CDMA: Either IS95A or IS95B";
		case 5:
			return "EVDO revision 0";
		case 6:
			return "EVDO revision A";
		case 7:
			return "1xRTT";
		case 8:
			return "HSDPA";
		case 9:
			return "HSUPA";
		case 10:
			return "HSPA";
		case 11:
			return "iDen";
		case 12:
			return "EVDO revision B";
		case 13:
			return "LTE";
		case 14:
			return "eHRPD";
		case 15:
			return "HSPA+";
		default:
			return "unknown";
		}
	}
}
