package com.vikings.sanguo.cache;

import com.vikings.sanguo.utils.StringUtil;

public class UserCommonCache extends FileCache {
	public static String FILE_NAME = "user_common.csv";
	private int maxStamina;// 行动力上限
	private int basePrice;// 回复行动力基础价（元宝）
	private int addPrice;// 回复行动力递增价（元宝）
	private int addTime;// 恢复周期(秒)
	private byte newerMaxLevel;// 新手等级
	private byte vipBlessFreeLevel;// vip特权开通免费最低玩家vip等级
	private int vipBlessPrice;// vip特权开通需要元宝数量
	private int vipBlessMinTroopCount;// 勇者无敌救济军最低兵力
	private int vipBlessCdReduce;// 勇者无敌金币招兵时间cd缩短百分比
	private byte newerMinLevel;// 新手开启等级
	private byte abandonMinLevel;// 分解将领开启等级

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return FILE_NAME;
	}

	public int getMaxStamina() {
		return maxStamina;
	}

	public int getBasePrice() {
		return basePrice;
	}

	public int getAddPrice() {
		return addPrice;
	}

	public byte getNewerMaxLevel() {
		return newerMaxLevel;
	}

	public byte getVipSpecialMinLevel() {
		return vipBlessFreeLevel;
	}

	public int getVipPrice() {
		return vipBlessPrice;
	}

	public int getVipBlessCdReduce() {
		return vipBlessCdReduce;
	}

	public byte getNewerMinLevel() {
		return newerMinLevel;
	}

	public int getVipBlessMinTroopCount() {
		return vipBlessMinTroopCount;
	}

	public byte getAbandonMinLevel() {
		return abandonMinLevel;
	}

	public int getAddTime() {
		return addTime;
	}

	@Override
	public Object fromString(String line) {
		StringBuilder buf = new StringBuilder(line);
		maxStamina = StringUtil.removeCsvInt(buf);
		basePrice = StringUtil.removeCsvInt(buf);
		addPrice = StringUtil.removeCsvInt(buf);
		addTime = StringUtil.removeCsvInt(buf);
		newerMaxLevel = StringUtil.removeCsvByte(buf);
		vipBlessFreeLevel = StringUtil.removeCsvByte(buf);
		vipBlessPrice = StringUtil.removeCsvInt(buf);
		StringUtil.removeCsv(buf);
		StringUtil.removeCsv(buf);
		StringUtil.removeCsv(buf);
		vipBlessMinTroopCount = StringUtil.removeCsvInt(buf);
		StringUtil.removeCsv(buf);
		StringUtil.removeCsv(buf);
		vipBlessCdReduce = StringUtil.removeCsvInt(buf);
		StringUtil.removeCsv(buf);
		StringUtil.removeCsv(buf);
		newerMinLevel = StringUtil.removeCsvByte(buf);
		// abandonMinLevel = StringUtil.removeCsvByte(buf);
		return line;
	}
}
