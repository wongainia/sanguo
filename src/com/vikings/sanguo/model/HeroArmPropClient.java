package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.HeroArmProp;
import com.vikings.sanguo.utils.ListUtil;

public class HeroArmPropClient {
	protected int type; // 兵种类型
	protected int value; // 数值
	protected int maxValue; // 数值上限
	protected HeroTroopName heroTroopName;

	protected HeroArmPropClient() {

	}

	public HeroArmPropClient(int type) throws GameException {
		this.type = type;
		heroTroopName = (HeroTroopName) CacheMgr.heroTroopNameCache.get(type);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	public void setHeroTroopName(HeroTroopName heroTroopName) {
		this.heroTroopName = heroTroopName;
	}

	public HeroTroopName getHeroTroopName() {
		return heroTroopName;
	}

	public static HeroArmPropClient convert(HeroArmProp info)
			throws GameException {
		if (null == info)
			return null;
		HeroArmPropClient hapc = new HeroArmPropClient(info.getType());
		hapc.setValue(info.getValue());
		hapc.setMaxValue(info.getMaxValue());

		HeroTroopName htn = (HeroTroopName) CacheMgr.heroTroopNameCache
				.get(info.getType());
		hapc.setHeroTroopName(htn);
		return hapc;
	}

	public static List<HeroArmPropClient> convert2List(List<HeroArmProp> infos)
			throws GameException {
		List<HeroArmPropClient> list = new ArrayList<HeroArmPropClient>();
		if (null == infos || infos.isEmpty())
			return list;
		for (HeroArmProp info : infos) {
			list.add(convert(info));
		}
		return list;
	}

	public static boolean isStrength(List<HeroArmPropClient> armProps,
			TroopProp tp) {
		if (ListUtil.isNull(armProps))
			return false;

		// 神兵
		if (7 == tp.getType())
			return true;

		for (HeroArmPropClient it : armProps) {
			if (tp.getType() == it.getType())
				return true;
		}
		return false;
	}

	/**
	 * 统率效果（相应兵种的攻击加成）
	 * 
	 * @param baseAttack
	 *            武将攻击力
	 * @return
	 */
	public double attackAddValue(int baseAttack, TroopProp troopProp) {
		// double add = (Math.sqrt(baseAttack + 100) * 40 + Math.sqrt(value +
		// 100)
		// * 4 - 440)
		// / 100 * troopProp.getAtkModulus() / 100;

		double add = (getValue() + baseAttack * 45) * troopProp.getAtkModulus()
				* 0.000001;
		return add;
	}

	public static double attackAddValue(int baseAttack, TroopProp troopProp,
			int value) {
		// double add = (Math.sqrt(baseAttack + 100) * 40 + Math.sqrt(value +
		// 100)
		// * 4 - 440)
		// / 100 * troopProp.getAtkModulus() / 100;
		double add = (value + baseAttack * 45) * troopProp.getAtkModulus()
				* 0.000001;
		return add;
	}

	/**
	 * 统率效果（相应兵种的防御加成）
	 * 
	 * @param baseDefend
	 *            武将防御力
	 * @return
	 */
	public double defendAddValue(int baseDefend, TroopProp troopProp) {
		// double add = (Math.sqrt(baseDefend + 100) * 40 + Math.sqrt(value +
		// 100)
		// * 4 - 440)
		// / 100 * troopProp.getDefModulus() / 100;

		double add = (getValue() + baseDefend * 45) * troopProp.getAtkModulus()
				* 0.000001;
		return add;
	}

	public static double defendAddValue(int baseDefend, TroopProp troopProp,
			int value) {
		// double add = (Math.sqrt(baseDefend + 100) * 40 + Math.sqrt(value +
		// 100)
		// * 4 - 440)
		// / 100 * troopProp.getDefModulus() / 100;

		double add = (value + baseDefend * 45) * troopProp.getAtkModulus()
				* 0.000001;
		return add;
	}

}
