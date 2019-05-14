/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-5-4 下午3:00:57
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import java.util.ArrayList;

public class Province {
	private int idx;       //省份索引
	private String name;   //省份名称
	private ArrayList<CityGeoInfo> cityGeoInfos;   //省内城市信息
	
	public void setCityGeoInfos(ArrayList<CityGeoInfo> cityGeoInfos) {
		this.cityGeoInfos = cityGeoInfos;
	}
	
	public void setIdx(int idx) {
		this.idx = idx;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public ArrayList<CityGeoInfo> getCityGeoInfos() {
		return cityGeoInfos;
	}
	
	public int getIdx() {
		return idx;
	}
	
	public String getName() {
		return name;
	}
}
