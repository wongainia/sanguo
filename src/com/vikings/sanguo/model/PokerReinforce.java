/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-11-6 下午2:57:23
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class PokerReinforce {
	private int type;
	private int cost;
	private int troopId;
	private String icon;
	private String desc;
	private String title;
	private int pokerType;  //翻牌类型1=增援翻牌2=单挑翻牌
	
	public static final int ASSIST_JUNIOR = 1;
	public static final int ASSIST_MIDDLE = 2;
	public static final int ASSIST_SENIOR = 3;
	public static final int SINGLED_JUNIOR = 4;
	public static final int SINGLED_MIDDLE = 5;
	public static final int SINGLED_SENIOR = 6;
	
	public static final int ASSIST_POKER = 1;
	public static final int SINFLED_POKER = 2;
	
	public void setCost(int cost) {
		this.cost = cost;
	}
	
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public void setTroopId(int troopId) {
		this.troopId = troopId;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public int getCost() {
		return cost;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public String getIcon() {
		return icon;
	}
	
	public int getTroopId() {
		return troopId;
	}
	
	public int getType() {
		return type;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setPokerType(int pokerType) {
		this.pokerType = pokerType;
	}
	
	public int getPokerType() {
		return pokerType;
	}
	
	static public PokerReinforce fromString(String line) {
		StringBuilder buf = new StringBuilder(line);
		PokerReinforce pr = new PokerReinforce();
		
		pr.setType(StringUtil.removeCsvInt(buf));
		pr.setCost(StringUtil.removeCsvInt(buf));
		pr.setTroopId(StringUtil.removeCsvInt(buf));
		StringUtil.removeCsv(buf);
		StringUtil.removeCsv(buf);
		StringUtil.removeCsv(buf);
		pr.setIcon(StringUtil.removeCsv(buf));
		pr.setDesc(StringUtil.removeCsv(buf));
		pr.setTitle(StringUtil.removeCsv(buf));
		pr.setPokerType(StringUtil.removeCsvInt(buf));
		
		return pr;
	} 
}
