package com.vikings.sanguo.model;


import java.io.Serializable;

import com.vikings.sanguo.utils.StringUtil;

public class EventRewards implements Serializable{
	/**
	 * 
	 */
	public static final int ID_UPDATE = 15;

	private byte type;// (1、活动；2奖励)

	private byte id; /*
					 * 1 VIP特权 2 商城 3 外敌入侵 4 铜雀台 5 凤仪亭 6 双倍奖励 7 包月优惠 8 任务 9 荣耀榜
					 * 10 巅峰战场 11 战神宝箱 12 每日签到 13 升级奖励 14 我要变强 15 游戏更新 16 天降横财
					 * 17 公告
					 */

	private String icon;

	private String name;

	private int sequence;

	private int jumpId;// 界面跳转id

	private String url;// 跳转url
	
	private int level;//开放等级
	
	private boolean firstFlash = false;       // 第一次闪烁   false没闪烁   为true表示已经第一次闪烁过

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public byte getId() {
		return id;
	}

	public void setId(byte id) {
		this.id = id;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public int getJumpId() {
		return jumpId;
	}

	public void setJumpId(int jumpId) {
		this.jumpId = jumpId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public boolean getFirstFlash() {
		return firstFlash;
	}

	public void setFirstFlash(boolean firstFlash) {
		this.firstFlash = firstFlash;
	}
	
	public int getMinLevel() {
		return level;
	}

	public void setMinLevel(int level) {
		this.level = level;
	}

	public static EventRewards fromString(String line) {
		EventRewards er = new EventRewards();
		StringBuilder buf = new StringBuilder(line);
		er.setType(StringUtil.removeCsvByte(buf));
		er.setId(StringUtil.removeCsvByte(buf));
		er.setIcon(StringUtil.removeCsv(buf));
		er.setName(StringUtil.removeCsv(buf));
		er.setSequence(StringUtil.removeCsvInt(buf));
		er.setJumpId(StringUtil.removeCsvInt(buf));
		er.setUrl(StringUtil.removeCsv(buf));
		er.setMinLevel(StringUtil.removeCsvInt(buf));
		return er;
	}	
}
