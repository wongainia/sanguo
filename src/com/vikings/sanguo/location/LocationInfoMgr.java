package com.vikings.sanguo.location;

import java.lang.reflect.Method;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.network.HttpConnector;

/**
 * 获取定位相关信息 包括基站信息和wifi信息
 * 
 * @author Brad.Chen
 * 
 */
public class LocationInfoMgr {

	private TelephonyManager tel;

	private WifiManager wifi;

	private int asu;

	// private boolean gsm = false;

	private boolean cdma = false;

	public LocationInfoMgr() {
		tel = (TelephonyManager) Config.getController().getSystemService(
				Context.TELEPHONY_SERVICE);
		tel.listen(new CellInfoListener(),
				PhoneStateListener.LISTEN_SIGNAL_STRENGTH);
		wifi = (WifiManager) Config.getController().getSystemService(
				Context.WIFI_SERVICE);
	}

	public static int dBm(int i) {
		int j;
		if (i >= 0 && i <= 31)
			j = i * 2 + -113;
		else
			j = 0;
		return j;
	}

	private JSONArray cellTowersInfo() {
		int cell_id = 0;
		int location_area_code = 0;
		int mobile_country_code = 0;
		int mobile_network_code = 0;
		int signal_strength = 0;

		String str = this.tel.getNetworkOperator();
		if ((str == null) || ((str.length() != 5) && (str.length() != 6)))
			str = this.tel.getSimOperator();
		str = str.trim();
		if (str.length() == 5 || str.length() == 6) {
			mobile_country_code = Integer.parseInt(str.substring(0, 3));
			mobile_network_code = Integer.parseInt(str.substring(3,
					str.length()));
		} else {
			mobile_country_code = 0;
			mobile_network_code = 0;
		}

		int phoneType = this.tel.getPhoneType();
		CellLocation cellLocation = this.tel.getCellLocation();
		// GSM手机
		if (phoneType == TelephonyManager.PHONE_TYPE_GSM
				&& cellLocation instanceof GsmCellLocation) {
			// gsm = true;
			GsmCellLocation gl = (GsmCellLocation) cellLocation;
			cell_id = gl.getCid();
			location_area_code = gl.getLac();
			signal_strength = dBm(this.asu);
		}
		// CDMA
		else if (phoneType == TelephonyManager.PHONE_TYPE_CDMA) {
			cdma = true;
			Class<?> clsCellLocation = cellLocation.getClass();
			Class<?>[] aryClass = new Class[0];
			try {
				Method localMethod1 = clsCellLocation.getMethod(
						"getBaseStationId", aryClass);
				Method localMethod2 = clsCellLocation.getMethod("getSystemId",
						aryClass);
				Method localMethod3 = clsCellLocation.getMethod("getNetworkId",
						aryClass);
				Object[] aryDummy = new Object[0];
				int bid = ((Integer) localMethod1
						.invoke(cellLocation, aryDummy)).intValue();
				int sid = ((Integer) localMethod2
						.invoke(cellLocation, aryDummy)).intValue();
				int nid = ((Integer) localMethod3
						.invoke(cellLocation, aryDummy)).intValue();
				cell_id = bid;
				location_area_code = nid;
				mobile_network_code = sid;
				signal_strength = this.asu;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		JSONArray array = new JSONArray();
		try {
			JSONObject obj = new JSONObject();
			obj.put("cell_id", cell_id)
					.put("location_area_code", location_area_code)
					.put("mobile_country_code", mobile_country_code)
					.put("mobile_network_code", mobile_network_code)
					.put("age", 0).put("signal_strength", signal_strength);
			array.put(obj);
			List<NeighboringCellInfo> ls = tel.getNeighboringCellInfo();
			if (ls != null) {
				for (NeighboringCellInfo ni : ls) {
					int cid = ni.getCid();
					if (cid != NeighboringCellInfo.UNKNOWN_CID
							&& cid <= 0xffffffff) {
						JSONObject neighbor = new JSONObject();
						neighbor.put("cell_id", cid)
								.put("location_area_code", location_area_code)
								.put("mobile_country_code", mobile_country_code)
								.put("mobile_network_code", mobile_network_code)
								.put("age", 0)
								.put("signal_strength", dBm(ni.getRssi()));
						array.put(neighbor);
					}
				}
			}
		} catch (JSONException e) {
		}
		return array;
	}

	private JSONArray wifiTowersInfo() {
		JSONArray array = new JSONArray();
		try {
			if (wifi.isWifiEnabled()) {
				WifiInfo info = wifi.getConnectionInfo();
				JSONObject obj = new JSONObject();
				obj.put("mac_address", info.getBSSID())
						.put("signal_strength", info.getRssi())
						.put("ssid", info.getSSID()).put("age", 0);
				array.put(obj);
			}
			List<ScanResult> rs = wifi.getScanResults();
			if (rs != null) {
				for (ScanResult r : rs) {
					JSONObject obj = new JSONObject();
					obj.put("mac_address", r.BSSID)
							.put("signal_strength", r.level)
							.put("ssid", r.level).put("age", 0);
					array.put(obj);
				}
			}
		} catch (Exception e) {
		}
		return array;
	}

	class CellInfoListener extends PhoneStateListener {

		@Override
		public void onSignalStrengthChanged(int asu) {
			super.onSignalStrengthChanged(asu);
			LocationInfoMgr.this.asu = asu;
		}

	}

	public Location startLoc() {
		try {
			JSONObject obj = new JSONObject();
			JSONArray array = cellTowersInfo();
			if (array.length() != 0) {
				obj.put("cell_towers", array);
			}
			array = wifiTowersInfo();
			if (array.length() != 0) {
				obj.put("wifi_towers", array);
			}
			obj.put("version", "1.1.0");
			obj.put("host", "maps.google.com");
			obj.put("access_token", "2:k7j3G6LaL6u_lafw:4iXOeOpTh1glSXe");
			// obj.put("home_mobile_country_code", 460);
			// obj.put("home_mobile_network_code", 0);
			obj.put("address_language", "zh_CN");
			String type = "gsm";
			if (cdma)
				type = "cdma";
			obj.put("radio_type", type);
			obj.put("request_address", false);

			for (int i = 0; i < 1; i++) {
				try {
					String result = HttpConnector.getInstance().httpPost(
							"http://www.google.com/loc/json", obj.toString());
					JSONObject rs = new JSONObject(result)
							.getJSONObject("location");
					Location loc = new Location(Constants.PROVIDER_CUSTOM);
					loc.setLatitude(getValue(rs, "latitude"));
					loc.setLongitude(getValue(rs, "longitude"));
					loc.setAltitude(getValue(rs, "altitude"));
					loc.setAccuracy((float) getValue(rs, "accuracy"));
					return loc;
				} catch (Exception e) {
					Log.e(LocationInfoMgr.class.getName(), "req google fail.", e);
				}
			}
		} catch (Exception e) {
			Log.e(LocationInfoMgr.class.getName(), "loc fail", e);
		}
		return null;
	}

	private double getValue(JSONObject j, String key) {
		try {
			return j.getDouble(key);
		} catch (JSONException e) {
			return 0;
		}
	}
}
