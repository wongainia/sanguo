/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-1-9 下午5:23:29
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class CityGeoInfo {
	private byte province;
	private byte city;
	private int longtitude;
	private int latitude;
	private String cityName;
	private String provinceName;

	public void setCity(byte city) {
		this.city = city;
	}

	public void setProvince(byte province) {
		this.province = province;
	}

	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}

	public void setLongtitude(int longtitude) {
		this.longtitude = longtitude;
	}

	public byte getCity() {
		return city;
	}

	public int getLatitude() {
		return latitude;
	}

	public int getLongtitude() {
		return longtitude;
	}

	public byte getProvince() {
		return province;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityName() {
		return cityName;
	}
	
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	
	public String getProvinceName() {
		return provinceName;
	}
	
	public static CityGeoInfo fromString(String line) {
		StringBuilder buf = new StringBuilder(line);
		CityGeoInfo info = new CityGeoInfo();

		info.setProvince(Byte.valueOf(StringUtil.removeCsv(buf)));
		info.setCity(Byte.valueOf(StringUtil.removeCsv(buf)));
		float lon = Float.valueOf(StringUtil.removeCsv(buf));
		info.setLongtitude((int) (lon * 1000000));
		float lat = Float.valueOf(StringUtil.removeCsv(buf));
		info.setLatitude((int) (lat * 1000000));
		info.setCityName(StringUtil.removeCsv(buf));
		info.setProvinceName(StringUtil.removeCsv(buf));
		return info;
	}
}
