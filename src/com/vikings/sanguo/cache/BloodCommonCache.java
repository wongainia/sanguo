package com.vikings.sanguo.cache;

import com.vikings.sanguo.utils.StringUtil;

public class BloodCommonCache extends FileCache {
	private final static String FILE_NAME = "blood_common.csv";

	private byte openLevel; // 开启等级
	private int costId; // 消耗道具
	private int costCount;// 消耗道具基数
	private int costAdd;// 消化道具增量
	private int maxTimes;// 每日最大次数
	private int maxArmCount;// 最大兵力上限
	private int recordReduce;// 关卡减量（下次挑战时的关卡）
	private int showPokers;// 展示牌的数量

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
		openLevel = StringUtil.removeCsvByte(buf);
		costId = StringUtil.removeCsvInt(buf);
		costCount = StringUtil.removeCsvInt(buf);
		costAdd = StringUtil.removeCsvInt(buf);
		maxTimes = StringUtil.removeCsvInt(buf);
		maxArmCount = StringUtil.removeCsvInt(buf);
		recordReduce = StringUtil.removeCsvInt(buf);
		showPokers = StringUtil.removeCsvInt(buf);
		return line;
	}

	public byte getOpenLevel() {
		return openLevel;
	}

	public int getCostId() {
		return costId;
	}

	public int getCostCount() {
		return costCount;
	}

	public int getCostAdd() {
		return costAdd;
	}

	public int getMaxTimes() {
		return maxTimes;
	}

	public int getMaxArmCount() {
		return maxArmCount;
	}

	public int getRecordReduce() {
		return recordReduce;
	}

	public int getShowPokers() {
		return showPokers;
	}

}
