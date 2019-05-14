package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.BytesUtil;
import com.vikings.sanguo.utils.StringUtil;

public class NpcClientProp {
	private int id;
	private int iconId;
	private byte sex;
	private String nickName;
	private byte level;
	private String signStr;
	private int birthday;
	private int money;
	private int exp;

	private String mapImg;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIconId() {
		return iconId;
	}

	public void setIconId(int iconId) {
		this.iconId = iconId;
	}

	public byte getSex() {
		return sex;
	}

	public void setSex(byte sex) {
		this.sex = sex;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public byte getLevel() {
		return level;
	}

	public void setLevel(byte level) {
		this.level = level;
	}

	public String getSignStr() {
		return signStr;
	}

	public void setSignStr(String signStr) {
		this.signStr = signStr;
	}

	public int getBirthday() {
		return birthday;
	}

	public void setBirthday(int birthday) {
		this.birthday = birthday;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public String getIconName() {
		return BytesUtil.getLong(getId(), getIconId()) + ".png";
	}
	
	public String getMapImg() {
		return mapImg;
	}
	
	public void setMapImg(String mapImg) {
		this.mapImg = mapImg;
	}
	
	public static NpcClientProp fromString(String csv) {
		NpcClientProp prop = new NpcClientProp();
		StringBuilder buf = new StringBuilder(csv);
		prop.setId(Integer.valueOf(StringUtil.removeCsv(buf)));
		prop.setIconId(Integer.valueOf(StringUtil.removeCsv(buf)));
		prop.setSex(Byte.valueOf(StringUtil.removeCsv(buf)));
		prop.setNickName(StringUtil.removeCsv(buf));
		prop.setLevel(Byte.valueOf(StringUtil.removeCsv(buf)));
		prop.setSignStr(StringUtil.removeCsv(buf));
		prop.setBirthday(Integer.valueOf(StringUtil.removeCsv(buf)));
		prop.setMoney(Integer.valueOf(StringUtil.removeCsv(buf)));
		prop.setExp(Integer.valueOf(StringUtil.removeCsv(buf)));
		prop.setMapImg(StringUtil.removeCsv(buf));
		return prop;
	}
}
