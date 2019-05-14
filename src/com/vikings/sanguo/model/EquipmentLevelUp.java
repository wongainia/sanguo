package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class EquipmentLevelUp {
	public static final byte EQUIPMENT_MAX_LEVEL = 99;

	public static final int CURRENCY_UNIT = 10000;

	public static final int TYPE_ATTR = 1;
	public static final int TYPE_ITEM = 2;
	public static final int TYPE_BUILDING = 3;
	public static final int TYPE_HERO = 4;
	public static final int TYPE_ARM = 5;
	public static final int TYPE_RECHARGE = 6;

	private int scheme;// 装备方案号
	private byte quality;// 品质
	private byte minLv;// 等级下限
	private byte maxLv;// 等级上限
	private short type;// 消耗类型
	private int effect;// 消耗id
	private int value;// 消耗数量
	private int currency;// 每1w数量折算元宝数
	private short key;// 客户端用，key

	public int getScheme() {
		return scheme;
	}

	public void setScheme(int scheme) {
		this.scheme = scheme;
	}

	public byte getQuality() {
		return quality;
	}

	public void setQuality(byte quality) {
		this.quality = quality;
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

	public short getType() {
		return type;
	}

	public void setType(short type) {
		this.type = type;
	}

	public int getEffect() {
		return effect;
	}

	public void setEffect(int effect) {
		this.effect = effect;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getCurrency() {
		return currency;
	}

	public void setCurrency(int currency) {
		this.currency = currency;
	}

	public short getKey() {
		return key;
	}

	public void setKey(short key) {
		this.key = key;
	}

	public EquipmentLevelUp copy() {
		EquipmentLevelUp levelUp = new EquipmentLevelUp();
		levelUp.setScheme(getScheme());
		levelUp.setQuality(getQuality());
		levelUp.setMinLv(getMinLv());
		levelUp.setMaxLv(getMaxLv());
		levelUp.setType(getType());
		levelUp.setEffect(getEffect());
		levelUp.setValue(getValue());
		levelUp.setCurrency(getCurrency());
		levelUp.setKey(getKey());
		return levelUp;
	}

	public static EquipmentLevelUp fromString(String line) {
		EquipmentLevelUp elu = new EquipmentLevelUp();
		StringBuilder buf = new StringBuilder(line);
		elu.setScheme(StringUtil.removeCsvInt(buf));
		elu.setQuality(StringUtil.removeCsvByte(buf));
		elu.setMinLv(StringUtil.removeCsvByte(buf));
		elu.setMaxLv(StringUtil.removeCsvByte(buf));
		elu.setType(StringUtil.removeCsvShort(buf));
		elu.setEffect(StringUtil.removeCsvInt(buf));
		elu.setValue(StringUtil.removeCsvInt(buf));
		elu.setCurrency(StringUtil.removeCsvInt(buf));
		elu.setKey(StringUtil.removeCsvShort(buf));
		return elu;
	}
}
