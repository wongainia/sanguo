/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-9-4 下午4:33:40
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class HeroEvolveDiscount {
	private int min;  //最小
	private int max;  //最大
	private int percent;  //物品原始元宝价格百分比
	
	public void setMax(int max) {
		this.max = max;
	}
	
	public void setMin(int min) {
		this.min = min;
	}
	
	public void setPercent(int percent) {
		this.percent = percent;
	}
	
	public int getMax() {
		return max;
	}
	
	public int getMin() {
		return min;
	}
	
	public int getPercent() {
		return percent;
	}
	
	public static HeroEvolveDiscount fromString(String line) {
		HeroEvolveDiscount discount = new HeroEvolveDiscount();
		StringBuilder buf = new StringBuilder(line);
		discount.setMin(StringUtil.removeCsvInt(buf));
		discount.setMax(StringUtil.removeCsvInt(buf));
		discount.setPercent(StringUtil.removeCsvInt(buf));
		return discount;
	}
}
