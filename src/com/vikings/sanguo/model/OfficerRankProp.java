package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

/**
 * 官衔
 * 
 * @author susong
 * 
 */
public class OfficerRankProp {
	private short id;
	private short rank;
	private String name;
	private int minFiefCount; // 最小领地数
	private int maxFiefCount; // 最大领地数

	public short getId() {
		return id;
	}

	public void setId(short id) {
		this.id = id;
	}

	public short getRank() {
		return rank;
	}

	public void setRank(short rank) {
		this.rank = rank;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMinFiefCount() {
		return minFiefCount;
	}

	public void setMinFiefCount(int minFiefCount) {
		this.minFiefCount = minFiefCount;
	}

	public int getMaxFiefCount() {
		return maxFiefCount;
	}

	public void setMaxFiefCount(int maxFiefCount) {
		this.maxFiefCount = maxFiefCount;
	}

	public static OfficerRankProp fromString(String csv) {
		OfficerRankProp prop = new OfficerRankProp();
		StringBuilder buf = new StringBuilder(csv);
		prop.setId(Short.valueOf(StringUtil.removeCsv(buf)));
		prop.setRank(Short.valueOf(StringUtil.removeCsv(buf)));
		prop.setName(StringUtil.removeCsv(buf));
		prop.setMinFiefCount(Integer.valueOf(StringUtil.removeCsv(buf)));
		prop.setMaxFiefCount(Integer.valueOf(StringUtil.removeCsv(buf)));
		return prop;
	}

}
