package com.vikings.sanguo.model;

import com.vikings.sanguo.protos.ArmInfo;
import com.vikings.sanguo.utils.StringUtil;

//血战战场兵种初始配置
public class BloodTroop implements Comparable<BloodTroop> {
	private int id;// 兵种id
	private int count;// 初始默认兵力
	private int limit; // 单兵种上限

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public static BloodTroop fromString(String line) {
		BloodTroop bloodTroop = new BloodTroop();
		StringBuilder buf = new StringBuilder(line);
		bloodTroop.setId(StringUtil.removeCsvInt(buf));
		bloodTroop.setCount(StringUtil.removeCsvInt(buf));
		bloodTroop.setLimit(StringUtil.removeCsvInt(buf));
		return bloodTroop;
	}
	
	public ArmInfo toArmInfo() {
		return new ArmInfo().setId(getId()).setCount(getCount());
	}

	@Override
	public int compareTo(BloodTroop another) {
		return getId() - another.getId();
	}
}
