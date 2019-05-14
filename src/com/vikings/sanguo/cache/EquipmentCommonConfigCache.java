package com.vikings.sanguo.cache;

import com.vikings.sanguo.utils.StringUtil;

public class EquipmentCommonConfigCache extends FileCache {
	private int rewardRate;// 趁热打铁赠送百分比
	private int rewardPeriod;// 趁热打铁持续时间（秒）
	private int reduceVaue;// 缓慢冷却每小时减少锻造值
	private int reducePeriod;// 缓慢冷却周期（秒）

	private static final String FILE_NAME = "equipment_common_config.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return "equipment_common_config";
	}

	@Override
	public Object fromString(String line) {
		StringBuilder buf = new StringBuilder(line);
		rewardRate = StringUtil.removeCsvInt(buf);
		rewardPeriod = StringUtil.removeCsvInt(buf);
		reduceVaue = StringUtil.removeCsvInt(buf);
		reducePeriod = StringUtil.removeCsvInt(buf);
		return line;
	}

	public int getRewardRate() {
		return rewardRate;
	}

	public int getRewardPeriod() {
		return rewardPeriod;
	}

	public int getReduceVaue() {
		return reduceVaue;
	}

	public int getReducePeriod() {
		return reducePeriod;
	}
}
