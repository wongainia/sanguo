package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class LevelUpDesc {

	private int level;
	private int seq;// 顺序(只显示排序1的)
	private int type;
	private String typeName;
	private String image;
	private String desc;
	private int toWindow; // 跳转界面（0无跳转，1跳转url）
	private String url; // 跳转URL
	private byte uiCtr;// 1无按钮，点击直接去跳转界面，2 有按钮，3点击任意位置关闭
	private int buildingType;

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
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

	public byte getUiCtr() {
		return uiCtr;
	}

	public void setUiCtr(byte uiCtr) {
		this.uiCtr = uiCtr;
	}

	public int getToWindow() {
		return toWindow;
	}

	public void setToWindow(int toWindow) {
		this.toWindow = toWindow;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getBuildingType() {
		return buildingType;
	}

	public void setBuildingType(int buildingType) {
		this.buildingType = buildingType;
	}

	public static LevelUpDesc fromString(String cvs) {
		LevelUpDesc lud = new LevelUpDesc();
		StringBuilder builder = new StringBuilder(cvs);
		lud.setLevel(StringUtil.removeCsvInt(builder));
		lud.setSeq(StringUtil.removeCsvInt(builder));
		lud.setType(StringUtil.removeCsvInt(builder));
		lud.setTypeName(StringUtil.removeCsv(builder));
		lud.setImage(StringUtil.removeCsv(builder));
		lud.setDesc(StringUtil.removeCsv(builder));
		lud.setToWindow(StringUtil.removeCsvInt(builder));
		lud.setUrl(StringUtil.removeCsv(builder));
		lud.setUiCtr(StringUtil.removeCsvByte(builder));
		lud.setBuildingType(StringUtil.removeCsvInt(builder));
		return lud;
	}

}
