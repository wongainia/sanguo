package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class ItemEffect {
	public static final int EFFECT_ID_ADD_HERO_EXP = 100;

	private int itemId;
	private int type;
	private int effectId;
	private int effectValue;
	private int effectValueAddtion;

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getEffectId() {
		return effectId;
	}

	public void setEffectId(int effectId) {
		this.effectId = effectId;
	}

	public int getEffectValue() {
		return effectValue;
	}

	public void setEffectValue(int effectValue) {
		this.effectValue = effectValue;
	}

	public int getEffectValueAddtion() {
		return effectValueAddtion;
	}

	public void setEffectValueAddtion(int effectValueAddtion) {
		this.effectValueAddtion = effectValueAddtion;
	}

	public static ItemEffect fromString(String line) {
		ItemEffect effect = new ItemEffect();
		StringBuilder buf = new StringBuilder(line);
		effect.setItemId(StringUtil.removeCsvInt(buf));
		effect.setType(StringUtil.removeCsvInt(buf));
		effect.setEffectId(StringUtil.removeCsvInt(buf));
		effect.setEffectValue(StringUtil.removeCsvInt(buf));
		effect.setEffectValueAddtion(StringUtil.removeCsvInt(buf));
		return effect;
	}

	public String toString() {
		return "" + itemId + type + effectId + effectValue + effectValueAddtion;
	}

}
