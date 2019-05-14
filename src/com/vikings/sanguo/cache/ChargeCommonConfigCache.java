package com.vikings.sanguo.cache;

import com.vikings.sanguo.utils.StringUtil;

public class ChargeCommonConfigCache extends FileCache {
	private final static String FILE_NAME = "charge_common_config.csv";
	private int chargeMax;// 短代最大累计充值积分（1元宝=1积分）
	private int doubleMin;// 双倍优惠充值最低要求（元宝）
	private int chargeRate;// 游戏消耗100元宝产生双倍积分
	private int monthMin;// 包月最低充值元宝
	private int monthReward;// 包月每日领取元宝
	private int monthDays;// 包月优惠周期（天）

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return FILE_NAME;
	}

	@Override
	public Object fromString(String line) {
		StringBuilder buf = new StringBuilder(line);
		chargeMax = StringUtil.removeCsvInt(buf);
		doubleMin = StringUtil.removeCsvInt(buf);
		chargeRate = StringUtil.removeCsvInt(buf);
		monthMin = StringUtil.removeCsvInt(buf);
		monthReward = StringUtil.removeCsvInt(buf);
		monthDays = StringUtil.removeCsvInt(buf);
		return line;
	}

	public int getChargeMax() {
		return chargeMax;
	}

	public int getDoubleMin() {
		return doubleMin;
	}

	public int getChargeRate() {
		return chargeRate;
	}

	public int getMonthMin() {
		return monthMin;
	}

	public int getMonthReward() {
		return monthReward;
	}

	public int getMonthDays() {
		return monthDays;
	}

}
