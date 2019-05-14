package com.vikings.sanguo.model;

import java.io.Serializable;

import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.utils.StringUtil;

/**
 * 士兵属性
 * 
 * @author susong
 * 
 */
public class TroopProp implements Serializable, Comparable<TroopProp> {

	private static final long serialVersionUID = 6310488553395682476L;

	// 神兵
	public static int GOD_TROOP = 7;
	// 精灵盾卫
	public static int SHIELD_GUARDIAN_ID = 101;

	private int id;
	private String name;
	private String icon; // 图标
	private String imageUp; // 战斗中的图片
	private String imageDown; // 战斗中的图片
	private String smallIcon;// 小图标
	private String desc;
	private byte type; // 1、步兵 2、骑兵 3、枪兵 4、弓兵
	private int hp; // 血量
	private int hpModulus;// 血量加成系数(数值扩大了100倍)
	private int damage;// 伤害
	private int attack;// 攻击
	private int defend;// 防御
	private int atkModulus;// 攻击系数
	private int defModulus;// 防御系数
	private int range; // 射程
	private int block;// 拦截
	private int dexterous;// 灵巧
	private int position;// 站位
	private int speed; // 速度
	private int critRate; // 暴击
	private int critMultiple;// 暴击倍数
	private int antiCrit;// 韧性(免爆率1=0.01%)
	private byte atkRange; // 射程类型（1近战、2远程）
	private byte play;// 上场(1上场、0不上场)
	private int capacity;// 负重
	private int foodConsume;// 1000人每小时粮草消耗
	private int food;// 1000人出征粮草消耗
	private byte armType;// 0为普通兵 1 为神兵
	private byte train;// 是否可以招募 0不可以、1可以
	private byte boss; // 是否为boss，0：否，1：是
	private byte revive;// 复活类型（0不复活，1士兵位，2BOSS位）

	private byte formation;// 战斗队形
	private byte costFoodLevel;// 士兵耗粮的玩家等级下限

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getImageUp() {
		return imageUp;
	}

	public void setImageUp(String imageUp) {
		this.imageUp = imageUp;
	}

	public String getImageDown() {
		return imageDown;
	}

	public void setImageDown(String imageDown) {
		this.imageDown = imageDown;
	}

	public String getSmallIcon() {
		return smallIcon;
	}

	public void setSmallIcon(String smallIcon) {
		this.smallIcon = smallIcon;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getHpModulus() {
		return hpModulus;
	}

	public void setHpModulus(int hpModulus) {
		this.hpModulus = hpModulus;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getDefend() {
		return defend;
	}

	public void setDefend(int defend) {
		this.defend = defend;
	}

	public int getAtkModulus() {
		return atkModulus;
	}

	public void setAtkModulus(int atkModulus) {
		this.atkModulus = atkModulus;
	}

	public int getDefModulus() {
		return defModulus;
	}

	public void setDefModulus(int defModulus) {
		this.defModulus = defModulus;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public int getBlock() {
		return block;
	}

	public void setBlock(int block) {
		this.block = block;
	}

	public int getDexterous() {
		return dexterous;
	}

	public void setDexterous(int dexterous) {
		this.dexterous = dexterous;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getCritRate() {
		return critRate;
	}

	public void setCritRate(int critRate) {
		this.critRate = critRate;
	}

	public int getCritMultiple() {
		return critMultiple;
	}

	public void setCritMultiple(int critMultiple) {
		this.critMultiple = critMultiple;
	}

	public int getAntiCrit() {
		return antiCrit;
	}

	public void setAntiCrit(int antiCrit) {
		this.antiCrit = antiCrit;
	}

	public byte getAtkRange() {
		return atkRange;
	}

	public void setAtkRange(byte atkRange) {
		this.atkRange = atkRange;
	}

	public boolean canPlay() {
		return play == 1;
	}

	public void setPlay(byte play) {
		this.play = play;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getFoodConsume() {
		if (costFoodLevel > Account.user.getLevel())
			return 0;
		else
			return foodConsume;
	}

	public void setFoodConsume(int foodConsume) {
		this.foodConsume = foodConsume;
	}

	public int getFood() {
		return food;
	}

	public void setFood(int food) {
		this.food = food;
	}

	public byte getArmType() {
		return armType;
	}

	public void setArmType(byte armType) {
		this.armType = armType;
	}

	public boolean isGodSoldier() {
		return armType == 1;
	}

	public boolean canTrain() {
		return train == 1;
	}

	public void setTrain(byte train) {
		this.train = train;
	}

	public boolean isBoss() {
		return boss == 1;
	}

	public void setBoss(byte boss) {
		this.boss = boss;
	}

	public byte getRevive() {
		return revive;
	}

	public void setRevive(byte revive) {
		this.revive = revive;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public byte getFormation() {
		return formation;
	}

	public void setFormation(byte formation) {
		this.formation = formation;
	}

	public byte getCostFoodLevel() {
		return costFoodLevel;
	}

	public void setCostFoodLevel(byte costFoodLevel) {
		this.costFoodLevel = costFoodLevel;
	}

	public static TroopProp fromString(String csv) {
		TroopProp prop = new TroopProp();
		StringBuilder buf = new StringBuilder(csv);
		prop.setId(StringUtil.removeCsvInt(buf));// id
		prop.setName(StringUtil.removeCsv(buf));// 兵种
		prop.setIcon(StringUtil.removeCsv(buf));// 头像
		prop.setImageUp(StringUtil.removeCsv(buf));
		prop.setImageDown(StringUtil.removeCsv(buf));
		prop.setSmallIcon(StringUtil.removeCsv(buf));
		prop.setDesc(StringUtil.removeCsv(buf));// 描述
		prop.setType(StringUtil.removeCsvByte(buf));// 兵种类型
		prop.setHp(StringUtil.removeCsvInt(buf));// 生命值
		prop.setHpModulus(StringUtil.removeCsvInt(buf));// 生命系数
		prop.setDamage(StringUtil.removeCsvInt(buf));// 基础伤害
		prop.setAttack(StringUtil.removeCsvInt(buf));// 攻击
		prop.setDefend(StringUtil.removeCsvInt(buf));// 防御
		prop.setAtkModulus(StringUtil.removeCsvInt(buf));// 攻击系数
		prop.setDefModulus(StringUtil.removeCsvInt(buf));// 防御系数
		prop.setRange(StringUtil.removeCsvInt(buf));// 射程
		prop.setBlock(StringUtil.removeCsvInt(buf));// 拦截
		prop.setDexterous(StringUtil.removeCsvInt(buf));// 灵巧
		prop.setPosition(StringUtil.removeCsvInt(buf));// 站位
		prop.setSpeed(StringUtil.removeCsvInt(buf));// 速度
		prop.setCritRate(StringUtil.removeCsvInt(buf));// 暴击率
		prop.setCritMultiple(StringUtil.removeCsvInt(buf));// 暴击倍数
		prop.setAntiCrit(StringUtil.removeCsvInt(buf));// 韧性(免爆率1=0.01%)
		prop.setAtkRange(StringUtil.removeCsvByte(buf));// 射程类型（1近战、2远程）
		prop.setPlay(StringUtil.removeCsvByte(buf));// 上场(1上场、0不上场)
		prop.setCapacity(StringUtil.removeCsvInt(buf));// 负重
		prop.setFoodConsume(StringUtil.removeCsvInt(buf));// 1000人每小时粮草消耗
		prop.setFood(StringUtil.removeCsvInt(buf));// 1000人出征粮草消耗
		prop.setArmType(StringUtil.removeCsvByte(buf));// 0为普通兵 1 为神兵
		StringUtil.removeCsv(buf);// 跳过饥饿死亡权重
		prop.setTrain(StringUtil.removeCsvByte(buf));// 是否可以招募 0不可以、1可以
		prop.setBoss(StringUtil.removeCsvByte(buf)); // 是否为boss，0：否，1：是
		prop.setRevive(StringUtil.removeCsvByte(buf));// 复活类型（0不复活，1士兵位，2BOSS位）
		prop.setFormation(StringUtil.removeCsvByte(buf));// 战斗队形(1小兵 2骑兵 3象兵
															// 4Boss)
		prop.setCostFoodLevel(StringUtil.removeCsvByte(buf));
		return prop;

	}

	private boolean unlock(TroopProp prop) {
		return Account.manorInfoClient.getArmids().contains(prop.getId());
	}

	@Override
	public int compareTo(TroopProp another) {
		if (unlock(this) && !unlock(another)) {
			return -1;
		} else if (!unlock(this) && unlock(another)) {
			return 1;
		} else {
			return this.getId() - another.getId();
		}
	}

	public TroopProp copy() {
		TroopProp prop = new TroopProp();
		prop.setId(id);// id
		prop.setName(name);// 兵种
		prop.setIcon(icon);// 头像
		prop.setImageUp(imageUp);
		prop.setImageDown(imageDown);
		prop.setSmallIcon(smallIcon);
		prop.setDesc(desc);// 描述
		prop.setType(type);// 兵种类型
		prop.setHp(hp);// 生命值
		prop.setHpModulus(hpModulus);// 生命系数
		prop.setDamage(damage);// 基础伤害
		prop.setAttack(attack);// 攻击
		prop.setDefend(defend);// 防御
		prop.setAtkModulus(atkModulus);// 攻击系数
		prop.setDefModulus(defModulus);// 防御系数
		prop.setRange(range);// 射程
		prop.setBlock(block);// 拦截
		prop.setDefModulus(defModulus);// 灵巧
		prop.setPosition(position);// 站位
		prop.setSpeed(speed);// 速度
		prop.setCritRate(critRate);// 暴击率
		prop.setCritMultiple(critMultiple);// 暴击倍数
		prop.setAntiCrit(antiCrit);// 韧性(免爆率1=0.01%)
		prop.setAtkRange(atkRange);// 射程类型（1近战、2远程）
		prop.setPlay(play);// 上场(1上场、0不上场)
		prop.setCapacity(capacity);// 负重
		prop.setFoodConsume(foodConsume);// 1000人每小时粮草消耗
		prop.setFood(food);// 1000人出征粮草消耗
		prop.setArmType(armType);// 0为普通兵 1 为神兵
		prop.setTrain(train);// 是否可以招募 0不可以、1可以
		prop.setBoss(boss); // 是否为boss，0：否，1：是
		prop.setRevive(revive);// 复活类型（0不复活，1士兵位，2BOSS位）
		prop.setFormation(formation);// 战斗队形(1小兵 2骑兵 3象兵
										// 4Boss)
		return prop;
	}

}
