package com.vikings.sanguo.model;

import java.io.Serializable;

import com.vikings.sanguo.utils.StringUtil;

public class HeroType implements Serializable {
	private static final long serialVersionUID = -2258372784714925278L;
	private byte talent; // 天赋
	private byte star; // 星级
	private int propRate;// 计算进化后属性增加所用系数
	private int soulId;// 进化所需将魂id
	private int soulRate;// 进化所需将魂数量系数
	private byte evolveLevel;// 进化所需等级

	public int getType() {
		return talent;
	}

	public void setType(byte talent) {
		this.talent = talent;
	}

	public int getStar() {
		return star;
	}

	public void setStar(byte star) {
		this.star = star;
	}

	public int getPropRate() {
		return propRate;
	}

	public void setPropRate(int propRate) {
		this.propRate = propRate;
	}

	public int getSoulId() {
		return soulId;
	}

	public void setSoulId(int soulId) {
		this.soulId = soulId;
	}

	public int getSoulRate() {
		return soulRate;
	}

	public void setSoulRate(int soulRate) {
		this.soulRate = soulRate;
	}

	public byte getEvolveLevel() {
		return evolveLevel;
	}

	public void setEvolveLevel(byte evolveLevel) {
		this.evolveLevel = evolveLevel;
	}

	public static HeroType fromString(String csv) {
		HeroType heroType = new HeroType();
		StringBuilder buf = new StringBuilder(csv);
		heroType.setStar(StringUtil.removeCsvByte(buf));
		heroType.setType(StringUtil.removeCsvByte(buf));
		heroType.setPropRate(StringUtil.removeCsvInt(buf));
		heroType.setSoulId(StringUtil.removeCsvInt(buf));
		heroType.setSoulRate(StringUtil.removeCsvInt(buf));
		heroType.setEvolveLevel(StringUtil.removeCsvByte(buf));
		return heroType;
	}
}
