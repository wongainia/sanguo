/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2012-7-22
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */

package com.vikings.sanguo.model;

import android.util.Log;

import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.utils.StringUtil;

public class GambleData {
	private byte serialNum; // 排序编号
	private int type; // type（1资源、2物品）
	private int itemId; // 物品id
	private int amount; // 物品数量
	private int probility; // 产生概率（在同一水果机类型下出现的概率）
	private byte machineType; // 水果机类型
	private String machineName; // 水果机名字
	private short price; // 抽奖价格（元宝）
	private Item item;

	public final static int RES = 1;
	public final static int ITEM = 2;
	
	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

	public void setMachineType(byte machineType) {
		this.machineType = machineType;
	}

	public void setPrice(short price) {
		this.price = price;
	}

	public void setProbility(int probility) {
		this.probility = probility;
	}

	public void setSerialNum(byte serialNum) {
		this.serialNum = serialNum;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public int getAmount() {
		return amount;
	}

	public int getItemId() {
		return itemId;
	}

	public String getMachineName() {
		return machineName;
	}

	public byte getMachineType() {
		return machineType;
	}

	public short getPrice() {
		return price;
	}

	public int getProbility() {
		return probility;
	}

	public byte getSerialNum() {
		return serialNum;
	}

	public Item getItem() {
		return item;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public static GambleData fromString(String s) {
		GambleData g = new GambleData();
		StringBuilder buf = new StringBuilder(s);
		g.setSerialNum(StringUtil.removeCsvByte(buf));
		g.setType(StringUtil.removeCsvInt(buf));
		g.setItemId(StringUtil.removeCsvInt(buf));
		g.setAmount(StringUtil.removeCsvInt(buf));
		g.setProbility(StringUtil.removeCsvInt(buf));
		g.setMachineType(StringUtil.removeCsvByte(buf));
		g.setMachineName(StringUtil.removeCsv(buf));
		g.setPrice(StringUtil.removeCsvShort(buf));
		if (2 == g.getType()) {
			try {
				g.setItem((Item) CacheMgr.itemCache.get(g.getItemId()));
			} catch (GameException e) {
				Log.e("GambleData", e.getMessage());
				g.setItem(null);
			}
		} else
			g.setItem(null);
		
		return g;
	}
}
