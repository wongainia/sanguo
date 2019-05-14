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

public class ItemWeight {

	private int type;
	private int id;
	private int weight;
	private String icon;
		
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public String getIcon() {
		return icon;
	}
	
	public static ItemWeight fromString(String line) {
		StringBuilder buf = new StringBuilder(line);
		ItemWeight c = new ItemWeight();
		c.setType(StringUtil.removeCsvInt(buf));
		c.setId(StringUtil.removeCsvInt(buf));
		c.setWeight(StringUtil.removeCsvInt(buf));
		c.setIcon(StringUtil.removeCsv(buf));
		return c;
	}
}
