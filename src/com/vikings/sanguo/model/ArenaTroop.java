package com.vikings.sanguo.model;

import com.vikings.sanguo.protos.ArmInfo;
import com.vikings.sanguo.utils.StringUtil;

//巅峰战场兵种初始配置
public class ArenaTroop implements Comparable<ArenaTroop> {
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

	public static ArenaTroop fromString(String line) {
		ArenaTroop arenaTroop = new ArenaTroop();
		StringBuilder buf = new StringBuilder(line);
		arenaTroop.setId(StringUtil.removeCsvInt(buf));
		arenaTroop.setCount(StringUtil.removeCsvInt(buf));
		arenaTroop.setLimit(StringUtil.removeCsvInt(buf));
		return arenaTroop;
	}
	
	public ArmInfo toArmInfo() {
		return new ArmInfo().setId(getId()).setCount(getCount());
	}

	@Override
	public int compareTo(ArenaTroop another) {
		return getId() - another.getId();
	}
}
