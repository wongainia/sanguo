/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-5-8 下午7:41:47
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class BattleBgProp {
	private int fiefType;  //fief_type（1主城，2资源点，3圣都，4名城，5重镇）
	private int country;  //国家（0标示不限）
	private String img;  //背景图
	
	private String battle_wall_up;  //
	private String battle_wall_down;
	
	private byte isShowWall;

	public void setCountry(int country) {
		this.country = country;
	}
	
	public void setFiefType(int fiefType) {
		this.fiefType = fiefType;
	}
	
	public void setImg(String img) {
		this.img = img;
	}
	
	public int getCountry() {
		return country;
	}
	
	public int getFiefType() {
		return fiefType;
	}
	
	public String getImg() {
		return img;
	}
	
	public String getBattleWallup() {
		return battle_wall_up;
	}
	
	public void setBattleWallup(String battle_wall_up) {
		 this.battle_wall_up = battle_wall_up;
	}
	
	public String getBattleWallDown() {
		return battle_wall_down;
	}
	
	public void setBattleWallDown(String battle_wall_down) {
		 this.battle_wall_down = battle_wall_down;
	}
	
	public byte getIsShowWall() {
		return isShowWall;
	}
	
	public void setShowWall(byte isShowWall) {
		 this.isShowWall = isShowWall;
	}
	
	public static BattleBgProp fromString(String line) {
		BattleBgProp bbp = new BattleBgProp();
		StringBuilder buf = new StringBuilder(line);
		
		bbp.setFiefType(StringUtil.removeCsvInt(buf));
		bbp.setCountry(StringUtil.removeCsvInt(buf));
		bbp.setShowWall(StringUtil.removeCsvByte(buf));
		
		bbp.setImg(StringUtil.removeCsv(buf));
		
		bbp.setBattleWallup(StringUtil.removeCsv(buf));		
		bbp.setBattleWallDown(StringUtil.removeCsv(buf));
		return bbp;
	}
	
}
