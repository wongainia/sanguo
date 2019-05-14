package com.vikings.sanguo.model;

import java.util.List;

import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.protos.AttrType;
import com.vikings.sanguo.protos.ReturnAttrInfo;
import com.vikings.sanguo.protos.RoleAttrInfo;

public abstract class AttrData {
	/*
	 * ATTR_TYPE_EXP = 2; //玩家经验 ATTR_TYPE_EXPLOIT = 3; //功勋 ATTR_TYPE_SCORE =
	 * 4; //成就值 ATTR_TYPE_LEVEL = 6; //玩家等级 ATTR_TYPE_CURRENCY = 7; //元宝
	 * ATTR_TYPE_GUILD_CREDIT = 8; //家族当前贡献值 ATTR_TYPE_GUILD_CREDIT_TOTAL =
	 * 9;//累计家族贡献值 ATTR_TYPE_STAMINA = 10; //用户体力 ATTR_TYPE_CHARGE = 11; //vip积分
	 * ATTR_TYPE_ROULETTE_GOOD = 12; // 转盘好感度
	 * 
	 * //20 - 50以上为资源属性 ATTR_TYPE_MONEY = 20; //金币 ATTR_TYPE_FOOD = 21; //粮草
	 * ATTR_TYPE_WOOD = 22; //木材 ATTR_TYPE_MATERIAL_0 = 23; //铁
	 * ATTR_TYPE_MATERIAL_1 = 24; //铜
	 * 
	 * //100为非用户属性 ATTR_TYPE_HERO_EXP = 100; //将领经验 ATTR_TYPE_HERO_STAMINA =
	 * 101; //将领体力 ATTR_TYPE_HERO_TYPE = 102; //将领品质 ATTR_TYPE_EAR = 103; //耳朵
	 */

	public AttrType[] attrTypeArray = { AttrType.ATTR_TYPE_MONEY,
			AttrType.ATTR_TYPE_FOOD, AttrType.ATTR_TYPE_WOOD,
			AttrType.ATTR_TYPE_MATERIAL_0, AttrType.ATTR_TYPE_MATERIAL_1 };

	protected abstract List<RoleAttrInfo> getRoleAttrInfos();

	protected abstract List<ReturnAttrInfo> getReturnAttrInfos();

	private int getAttrRole(AttrType type, List<RoleAttrInfo> ls) {
		if (ls == null)
			return 0;
		for (RoleAttrInfo a : ls) {
			if (AttrType.valueOf(a.getId()) == type)
				return a.getValue();
		}
		return 0;
	}

	private void setAttrRole(AttrType type, List<RoleAttrInfo> ls, int value) {
		if (ls == null)
			return;
		for (RoleAttrInfo a : ls) {
			if (AttrType.valueOf(a.getId()) == type) {
				a.setValue(value);
				return;
			}
		}
		ls.add(new RoleAttrInfo().setId(type.getNumber()).setValue(value));
	}

	private int getAttrReturn(AttrType type, List<ReturnAttrInfo> ls) {
		if (ls == null)
			return 0;
		for (ReturnAttrInfo a : ls) {
			if (AttrType.valueOf(a.getType()) == type)
				return a.getValue();
		}
		return 0;
	}

	private void setAttrReturn(AttrType type, List<ReturnAttrInfo> ls, int value) {
		if (ls == null)
			return;
		for (ReturnAttrInfo a : ls) {
			if (AttrType.valueOf(a.getType()) == type) {
				a.setValue(value);
				return;
			}
		}
		ls.add(new ReturnAttrInfo().setType(type.getNumber()).setValue(value));
	}

	public int getAttr(AttrType type) {
		if (getReturnAttrInfos() != null)
			return getAttrReturn(type, getReturnAttrInfos());
		else
			return getAttrRole(type, getRoleAttrInfos());
	}

	private void setAttr(AttrType type, int value) {
		if (getReturnAttrInfos() != null)
			setAttrReturn(type, getReturnAttrInfos(), value);
		else
			setAttrRole(type, getRoleAttrInfos(), value);
	}

	public void setExp(int v) {
		setAttr(AttrType.ATTR_TYPE_EXP, v);
	}

	public int getExp() {
		return getAttr(AttrType.ATTR_TYPE_EXP);
	}

	public void setExploit(int v) {
		setAttr(AttrType.ATTR_TYPE_EXPLOIT, v);
	}

	public int getExploit() {
		return getAttr(AttrType.ATTR_TYPE_EXPLOIT);
	}

	public void setScore(int v) {
		setAttr(AttrType.ATTR_TYPE_SCORE, v);
	}

	public int getScore() {
		return getAttr(AttrType.ATTR_TYPE_SCORE);
	}

	public void setLevel(int v) {
		setAttr(AttrType.ATTR_TYPE_LEVEL, v);
	}

	public int getLevel() {
		return getAttr(AttrType.ATTR_TYPE_LEVEL);
	}

	public void setCurrency(int v) {
		setAttr(AttrType.ATTR_TYPE_CURRENCY, v);
	}

	public int getCurrency() {
		return getAttr(AttrType.ATTR_TYPE_CURRENCY);
	}

	public void setStamina(int v) {
		setAttr(AttrType.ATTR_TYPE_STAMINA, v);
	}

	protected int getStamina() {
		return getAttr(AttrType.ATTR_TYPE_STAMINA);
	}

	public void setMoney(int v) {
		setAttr(AttrType.ATTR_TYPE_MONEY, v);
	}

	public int getMoney() {
		return getAttr(AttrType.ATTR_TYPE_MONEY);
	}

	public void setFood(int v) {
		setAttr(AttrType.ATTR_TYPE_FOOD, v);
	}

	public int getFood() {
		return getAttr(AttrType.ATTR_TYPE_FOOD);
	}

	public void setWood(int v) {
		setAttr(AttrType.ATTR_TYPE_WOOD, v);
	}

	public int getWood() {
		return getAttr(AttrType.ATTR_TYPE_WOOD);
	}

	public void setMaterial0(int v) {
		setAttr(AttrType.ATTR_TYPE_MATERIAL_0, v);
	}

	public int getMaterial0() {
		return getAttr(AttrType.ATTR_TYPE_MATERIAL_0);
	}

	public void setMaterial1(int v) {
		setAttr(AttrType.ATTR_TYPE_MATERIAL_1, v);
	}

	public int getMaterial1() {
		return getAttr(AttrType.ATTR_TYPE_MATERIAL_1);
	}

	public void setHeroExp(int v) {
		setAttr(AttrType.ATTR_TYPE_HERO_EXP, v);
	}

	public void setHeroStamina(int v) {
		setAttr(AttrType.ATTR_TYPE_HERO_STAMINA, v);
	}

	public void setHeroType(int v) {
		setAttr(AttrType.ATTR_TYPE_HERO_TYPE, v);
	}

	public int getHeroExp() {
		return getAttr(AttrType.ATTR_TYPE_HERO_EXP);
	}

	public int getHeroStamina() {
		return getAttr(AttrType.ATTR_TYPE_HERO_STAMINA);
	}

	public int getHeroType() {
		return getAttr(AttrType.ATTR_TYPE_HERO_TYPE);
	}

	public int getCharge() { // vip积分
		return getAttr(AttrType.ATTR_TYPE_CHARGE);
	}

	// 根据vip积分取vip信息
	public UserVip getCurVip() {
		int charge = getCharge();
		if (charge < 0)
			charge = 0;
		return CacheMgr.userVipCache.getVipByCharge(charge);
	}

	public void setCharge(int charge) {
		setAttr(AttrType.ATTR_TYPE_CHARGE, charge);
	}

	// 服务器放大了10倍
	public int getRouletteGood() { // 轮盘好感度
		return getAttr(AttrType.ATTR_TYPE_ROULETTE_GOOD);
	}

	public float getRealRouletteGood() {
		return getRouletteGood() / 10f;
	}

	public void setRouletteGood(int good) {
		setAttr(AttrType.ATTR_TYPE_ROULETTE_GOOD, good);
	}

	public static boolean isShowAttr(int type) {
		if (type < AttrType.ATTR_TYPE_HERO_EXP.number)
			return true;

		return false;
	}

	public static boolean isResource(int type) {
		if (type == AttrType.ATTR_TYPE_FOOD.number
				|| type == AttrType.ATTR_TYPE_MONEY.number)
			return true;
		return false;
	}

	public static boolean isMaterial(int type) {
		if (type == AttrType.ATTR_TYPE_WOOD.getNumber()
				|| type == AttrType.ATTR_TYPE_MATERIAL_0.number
				|| type == AttrType.ATTR_TYPE_MATERIAL_1.number)
			return true;
		return false;
	}
}
