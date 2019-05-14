package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

/**
 * 军衔
 * 
 * @author susong
 * 
 */
public class WarRankProp {
	private int id;
	private int rank;
	private String name;
	private int militaryExploit; // 军功
	private int rmb; // 元宝购买
	private String desc; // 解锁相关描述
	private String descGain; // 解锁后描述
	private String upgradeDesc; // 升级时描述
	private String icon;
	private int validate; // 是否开放 0不开放，1开放

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMilitaryExploit() {
		return militaryExploit;
	}

	public void setMilitaryExploit(int militaryExploit) {
		this.militaryExploit = militaryExploit;
	}

	public int getRmb() {
		return rmb;
	}

	public void setRmb(int rmb) {
		this.rmb = rmb;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDescGain() {
		return descGain;
	}

	public void setDescGain(String descGain) {
		this.descGain = descGain;
	}

	public String getUpgradeDesc() {
		return upgradeDesc;
	}

	public void setUpgradeDesc(String upgradeDesc) {
		this.upgradeDesc = upgradeDesc;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getValidate() {
		return validate;
	}

	public void setValidate(int validate) {
		this.validate = validate;
	}

	public boolean isValidate() {
		return validate == 1;
	}

	public static WarRankProp fromString(String csv) {
		WarRankProp prop = new WarRankProp();
		StringBuilder buf = new StringBuilder(csv);
		prop.setId(Integer.valueOf(StringUtil.removeCsv(buf)));
		prop.setRank(Integer.valueOf(StringUtil.removeCsv(buf)));
		prop.setName(StringUtil.removeCsv(buf));
		prop.setMilitaryExploit(Integer.valueOf(StringUtil.removeCsv(buf)));
		prop.setRmb(Integer.valueOf(StringUtil.removeCsv(buf)));
		prop.setDesc(StringUtil.removeCsv(buf));
		prop.setDescGain(StringUtil.removeCsv(buf));
		prop.setUpgradeDesc(StringUtil.removeCsv(buf));
		prop.setIcon(StringUtil.removeCsv(buf));
		StringUtil.removeCsv(buf);
		StringUtil.removeCsv(buf);
		prop.setValidate(Integer.valueOf(StringUtil.removeCsv(buf)));
		return prop;
	}

}
