package com.vikings.sanguo.model;

import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.utils.StringUtil;

public class PropActType {
	private byte type; // 副本类型（2:战役副本, 98:巅峰， 99 血战）
	private String icon; // 副本图片
	private byte open; // 是否开放（0为不开放，1为开放）
	private byte openLvl; // 副本开放等级（不到等级不显示出来）
	private String name; // 副本名称
	private String desc; // 副本介绍
	private String nameIcon; // 副本名称图片文件名（新版用）

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOpen(byte open) {
		this.open = open;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public void setNameIcon(String nameIcon) {
		this.nameIcon = nameIcon;
	}

	public String getDesc() {
		return desc;
	}

	public String getIcon() {
		return icon;
	}

	public String getName() {
		return name;
	}

	public int getType() {
		return type;
	}

	public boolean isOpen() {
		return open == 1;
	}

	public void setOpenLvl(byte openLvl) {
		this.openLvl = openLvl;
	}
	//开放等级  和活动那里统一读取  event_rewards
	public byte getOpenLvl() {
//		EventRewards eventRewards = null;		
//		if(type == 99)
//		{
//			eventRewards = (EventRewards) CacheMgr.eventRewardsCache.search(18);			
//		}
//		else if(type == 98)
//		{
//			eventRewards = (EventRewards) CacheMgr.eventRewardsCache.search(19);			
//		}
//		if(eventRewards != null)
//		{
//			openLvl = (byte) eventRewards.getMinLevel();
//		}
		return openLvl;
	}

	public String getNameIcon() {
		return nameIcon;
	}

	public boolean isArena() {
		return type == 98;
	}

	public boolean isBlood() {
		return type == 99;
	}

	public boolean isShow() {
		return (Account.user.getLevel() >= openLvl);
	}

	public static PropActType fromString(String line) {
		StringBuilder buf = new StringBuilder(line);
		PropActType at = new PropActType();

		at.setType(StringUtil.removeCsvByte(buf));
		at.setIcon(StringUtil.removeCsv(buf));
		at.setOpen(StringUtil.removeCsvByte(buf));
		at.setOpenLvl(StringUtil.removeCsvByte(buf));
		at.setName(StringUtil.removeCsv(buf));
		at.setDesc(StringUtil.removeCsv(buf));
		at.setNameIcon(StringUtil.removeCsv(buf));

		return at;
	}
}
