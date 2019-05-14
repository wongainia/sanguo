package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class BonusProp implements Comparable<BonusProp> {

	private int id;

	private String title;

	private String desc;

	private String icon;

	private int verCode;

	private int specialType;// 与quest表中相对应

	// 铁血战神
	public static final int TIEXUE_ZHANSHEN = 1;
	// 家族之光
	public static final int JIAZHU_ZHIGUANG = 2;
	// 一掷千金
	public static final int YIZHI_QIANJIN = 3;
	// 威震江湖
	public static final int WEIZHEN_JIANGHU = 4;

	public static final int TYPE_UPDATE = 11;
	public static final int TYPE_VIP = 12;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getVerCode() {
		return verCode;
	}

	public void setVerCode(int verCode) {
		this.verCode = verCode;
	}

	public int getSpecialType() {
		return specialType;
	}

	public void setSpecialType(int specialType) {
		this.specialType = specialType;
	}

	public static BonusProp fromString(String line) {
		StringBuilder buf = new StringBuilder(line);
		BonusProp b = new BonusProp();
		b.setId(StringUtil.removeCsvInt(buf));
		b.setTitle(StringUtil.removeCsv(buf));
		b.setDesc(StringUtil.removeCsv(buf));
		b.setIcon(StringUtil.removeCsv(buf));
		b.setVerCode(StringUtil.removeCsvInt(buf));
		b.setSpecialType(StringUtil.removeCsvInt(buf));
		return b;
	}

	@Override
	public int compareTo(BonusProp another) {
		return this.id - another.id;
	}

	/**
	 * 是否过期
	 * 
	 * @return true：过期
	 */
	public boolean isExpired() {
		// if (start != 0 && start > Config.serverTimeSS())
		// return true;
		// if (end != 0 && end < Config.serverTimeSS())
		// return true;

		return false;
	}

}
