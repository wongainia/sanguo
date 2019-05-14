package com.vikings.sanguo.model;

public class BattlePropDefine {

	/*
	 * 属性（0兵种ID、1生命、2伤害、3攻击、4防御、5射程、6拦截、7灵巧、8站位、9速度、10暴击、11 暴击倍数、 12
	 * 韧性、13兵种类型、14攻击类型、15神兵、16数量、17还击）
	 */
	public static final int PROP_ARM_ID = 0;
	public static final int PROP_LIFE = 1;
	public static final int PROP_DAMAGE = 2;
	public static final int PROP_ATTACK = 3;
	public static final int PROP_DEFEND = 4;
	public static final int PROP_RANGE = 5;
	public static final int PROP_BLOCK = 6;
	public static final int PROP_DEXTEROUS = 7;
	public static final int PROP_POSITION = 8;
	public static final int PROP_SPEED = 9;
	public static final int PROP_CRIT = 10;
	public static final int PROP_CRIT_MULTIPLE = 11;
	public static final int PROP_ANTICRIT = 12;
	public static final String[] desc = { "兵种ID", "生命", "伤害", "攻击", "防御", "射程",
			"拦截", "灵巧", "站位", "速度", "暴击率", "伤害", "韧性" };
	public static final int[] propID = { PROP_LIFE, PROP_DAMAGE, PROP_ATTACK,
			PROP_DEFEND, PROP_RANGE, PROP_BLOCK, PROP_DEXTEROUS, PROP_POSITION,
			PROP_SPEED, PROP_CRIT, PROP_CRIT_MULTIPLE, PROP_ANTICRIT };

	public static String getPropDesc(int propId) {
		return desc[propId];
	}

}
