package com.vikings.sanguo.ui.map.core;

import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.location.Address;

import com.vikings.sanguo.network.HttpConnector;

public class Geocoder {

	private String url = "http://restapi.amap.com/rgeocode/simple?sid=7001&ia=1&region=#lon#,#lat#&poinum=1&range=1000&resType=json&roadnum=1&crossnum=0&key=1e82ee91c67e6187b7c2d5275bcca586&encode=utf-8";

	public Address getFromLocation(double latitude, double longitude) {
		try {
			Address a = new Address(Locale.CHINA);
			JSONObject rs = new JSONObject(HttpConnector.getInstance().httpGet(
					url.replace("#lat#", latitude + "").replace("#lon#",
							longitude + "")));
			if (!rs.has("list"))
				return a;
			JSONArray jj = rs.getJSONArray("list");
			if (jj.length() == 0)
				return a;
			rs = jj.getJSONObject(0);
			if (rs.has("province")) {
				a.setAdminArea(rs.getJSONObject("province").getString("name"));
			}
			if (rs.has("city")) {
				a.setLocality(rs.getJSONObject("city").getString("name"));
			}
			if (rs.has("district")) {
				a.setSubLocality(rs.getJSONObject("district").getString("name"));
			}
			if (rs.has("roadlist")) {
				JSONArray ja = rs.getJSONArray("roadlist");
				if (ja.length() > 0) {
					a.setFeatureName(ja.getJSONObject(0).getString("name"));
				}
			}
			if (rs.has("poilist")) {
				JSONArray ja = rs.getJSONArray("poilist");
				if (ja.length() > 0) {
					a.setFeatureName(ja.getJSONObject(0).getString("name"));
				}
			}
			return a;
		} catch (Exception e) {
			e.printStackTrace();
			return new Address(Locale.CHINA);
		}
	}
}
