package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class EquipmentForgeEffect {

	private int id; // 效果 ID
	private String effectName;// 效果名称
	private int type;// 类型
	private int level;// 等级
	private String effectDesc;// 效果描述
	private int validArm;// 有效兵种
	private int effectAtrr;// 效果属性
	private int additionWay;// 附加方式(1加基础值 2加百分比 3加最终值)
	private int effectValue;// 效果值

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEffectName() {
		return effectName;
	}

	public void setEffectName(String effectName) {
		this.effectName = effectName;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getEffectDesc() {
		return effectDesc;
	}

	public void setEffectDesc(String effectDesc) {
		this.effectDesc = effectDesc;
	}

	public int getValidArm() {
		return validArm;
	}

	public void setValidArm(int validArm) {
		this.validArm = validArm;
	}

	public int getEffectAtrr() {
		return effectAtrr;
	}

	public void setEffectAtrr(int effectAtrr) {
		this.effectAtrr = effectAtrr;
	}

	public int getAdditionWay() {
		return additionWay;
	}

	public void setAdditionWay(int additionWay) {
		this.additionWay = additionWay;
	}

	public int getEffectValue() {
		return effectValue;
	}

	public void setEffectValue(int effectValue) {
		this.effectValue = effectValue;
	}
	
	public static boolean isValidId(int effectId) {
		return effectId > 0;
	}

	public static EquipmentForgeEffect fromString(String csv) {
		EquipmentForgeEffect equipmentForgeEffect = new EquipmentForgeEffect();
		StringBuilder buf = new StringBuilder(csv);
		equipmentForgeEffect.setId(StringUtil.removeCsvInt(buf));
		equipmentForgeEffect.setEffectName(StringUtil.removeCsv(buf));
		equipmentForgeEffect.setType(StringUtil.removeCsvInt(buf));
		equipmentForgeEffect.setLevel(StringUtil.removeCsvInt(buf));
		equipmentForgeEffect.setEffectDesc(StringUtil.removeCsv(buf));
		equipmentForgeEffect.setValidArm(StringUtil.removeCsvInt(buf));
		equipmentForgeEffect.setEffectAtrr(StringUtil.removeCsvInt(buf));
		equipmentForgeEffect.setAdditionWay(StringUtil.removeCsvInt(buf));
		equipmentForgeEffect.setEffectValue(StringUtil.removeCsvInt(buf));
		return equipmentForgeEffect;
	}
}
