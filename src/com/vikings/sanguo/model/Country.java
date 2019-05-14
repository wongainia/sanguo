/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-5-4 下午4:31:34
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class Country {
	private int countryId;  //国家ID 
	private int provinceId;  //省份
	private String name;  //名称
	
	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}
	
	public int getCountryId() {
		return countryId;
	}
	
	public String getName() {
		return name;
	}
	
	public int getProvinceId() {
		return provinceId;
	}
	
	public static Country fromString(String line) {
		StringBuilder buf = new StringBuilder(line);
		Country c = new Country();
		c.setCountryId(StringUtil.removeCsvInt(buf));
		c.setProvinceId(StringUtil.removeCsvInt(buf));
		c.setName(StringUtil.removeCsv(buf));
		return c;
	}
}
