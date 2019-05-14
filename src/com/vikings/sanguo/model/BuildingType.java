package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

/**
 * 建筑类别
 * 
 * @author susong
 * 
 */
public class BuildingType {
	public static final byte MANOR_BUILDING_TYPE = 1;
	public static final byte FUNC_BUILDING_TYPE = 2;
	public static final byte RESIDENCE_BUILDING_TYPE = 3; // 住宅
	public static final byte FACTORY_BUILDING_TYPE = 4;
	public static final byte SHOP_BUILDING_TYPE = 5;
	public static final byte DECORATE_BUILDING_TYPE = 6; // 装饰
	public static final byte BANK_BUILDING_TYPE = 7; // 银行
	public static final byte PRESENT_BUILDING_TYPE = 8; // 赠送建筑
	public static final byte FIEF_BUILDING_TYPE = 9; // 士兵训练建筑

	private short type;
	private String name;
	private String nameImg;
	private String image;
	private String desc;
	private int openLv;
	private int seq;

	public short getType() {
		return type;
	}

	public void setType(short type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getNameImg() {
		return nameImg;
	}

	public void setNameImg(String nameImg) {
		this.nameImg = nameImg;
	}

	public int getOpenLv() {
		return openLv;
	}

	public void setOpenLv(int openLv) {
		this.openLv = openLv;
	}

	public static BuildingType fromString(String csv) {
		BuildingType bt = new BuildingType();
		StringBuilder buf = new StringBuilder(csv);
		bt.setType(StringUtil.removeCsvShort(buf));
		bt.setName(StringUtil.removeCsv(buf));
		bt.setNameImg(StringUtil.removeCsv(buf));
		bt.setImage(StringUtil.removeCsv(buf));
		bt.setDesc(StringUtil.removeCsv(buf));
		bt.setOpenLv(StringUtil.removeCsvInt(buf));
		bt.setSeq(Integer.valueOf(StringUtil.removeCsv(buf)));
		return bt;
	}

}
