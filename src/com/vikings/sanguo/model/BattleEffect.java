package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class BattleEffect {

	private int id; // 效果 ID
	private int effectFormula;// 效果公式ID
	private int para1;
	private int para2;
	private int para3;
	private int key;

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEffectFormula() {
		return effectFormula;
	}

	public void setEffectFormula(int effectFormula) {
		this.effectFormula = effectFormula;
	}

	public int getPara1() {
		return para1;
	}

	public void setPara1(int para1) {
		this.para1 = para1;
	}

	public int getPara2() {
		return para2;
	}

	public void setPara2(int para2) {
		this.para2 = para2;
	}

	public int getPara3() {
		return para3;
	}

	public void setPara3(int para3) {
		this.para3 = para3;
	}

	public static BattleEffect fromString(String csv) {
		BattleEffect battleEffect = new BattleEffect();
		StringBuilder buf = new StringBuilder(csv);
		battleEffect.setId(StringUtil.removeCsvInt(buf));
		battleEffect.setEffectFormula(StringUtil.removeCsvInt(buf));
		battleEffect.setPara1(StringUtil.removeCsvInt(buf));
		battleEffect.setPara2(StringUtil.removeCsvInt(buf));
		battleEffect.setPara3(StringUtil.removeCsvInt(buf));
		StringUtil.removeCsvInt(buf);// 参数4
		battleEffect.setKey(StringUtil.removeCsvInt(buf));
		return battleEffect;
	}
}
