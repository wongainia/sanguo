package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class EquipmentEffect {
	public static final byte EFFECT_TYPE_ATTACK = 1;
	public static final byte EFFECT_TYPE_DEFEND = 2;
	private int equipmentId;
	private byte quality;
	private byte effectType;
	private byte minLv;
	private byte maxLv;
	private int initValue;
	private int addValue;
	private String effectDesc;

	public int getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(int equipmentId) {
		this.equipmentId = equipmentId;
	}

	public byte getQuality() {
		return quality;
	}

	public void setQuality(byte quality) {
		this.quality = quality;
	}

	public byte getEffectType() {
		return effectType;
	}

	public void setEffectType(byte effectType) {
		this.effectType = effectType;
	}

	public String getEffectTypeName() {
		return getEffectTypeName(effectType);
	}

	public static String getEffectTypeName(byte effectType) {
		String name = "";
		switch (effectType) {
		case EFFECT_TYPE_ATTACK:
			name = "武力";
			break;
		case EFFECT_TYPE_DEFEND:
			name = "防护";
			break;
		default:
			break;
		}
		return name;
	}

	public byte getMinLv() {
		return minLv;
	}

	public void setMinLv(byte minLv) {
		this.minLv = minLv;
	}

	public byte getMaxLv() {
		return maxLv;
	}

	public void setMaxLv(byte maxLv) {
		this.maxLv = maxLv;
	}

	public int getInitValue() {
		return initValue;
	}

	public void setInitValue(int initValue) {
		this.initValue = initValue;
	}

	public int getAddValue() {
		return addValue;
	}

	public void setAddValue(int addValue) {
		this.addValue = addValue;
	}

	public int getEffectValue(int curLevel) {
		int effectValue = initValue;
		effectValue += (curLevel - minLv) * addValue;
		return effectValue;
	}

	public String getEffectDesc() {
		return effectDesc;
	}

	public void setEffectDesc(String effectDesc) {
		this.effectDesc = effectDesc;
	}

	public String getEffectDesc(int value) {
		return getEffectDesc().replace("<number>", "" + value);
	}

	public static EquipmentEffect fromString(String line) {
		EquipmentEffect ee = new EquipmentEffect();
		StringBuilder buf = new StringBuilder(line);
		ee.setEquipmentId(StringUtil.removeCsvInt(buf));
		ee.setQuality(StringUtil.removeCsvByte(buf));
		ee.setEffectType(StringUtil.removeCsvByte(buf));
		ee.setMinLv(StringUtil.removeCsvByte(buf));
		ee.setMaxLv(StringUtil.removeCsvByte(buf));
		ee.setInitValue(StringUtil.removeCsvInt(buf));
		ee.setAddValue(StringUtil.removeCsvInt(buf));
		ee.setEffectDesc(StringUtil.removeCsv(buf));
		return ee;
	}
}
