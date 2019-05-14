/**
\ *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-1-24 上午11:32:00
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

/**
 * 对应holy prop
 * 
 * @author chenqing
 * 
 */
public class HolyPropCfg {
	private int propId; // 圣城所在地fiefid
	private String name; // 名称
	private String scaleDesc; // 规模描述
	private String icon;
	private String evilDoorName; // 外敌入侵地名称
	private String desc;// 外敌入侵地描述
	private String bonusDesc; // 掉落描述
	private int canBePlayerOccupied; // 能否被玩家占领 （0：不能，1：能）
	private int lockTime;// 围城时间
	private int defenseBuff;// 城防加成（1/100，对士兵核心属性加成） , 城防加成技能ID
	private int minArmCountOpenDoor;// 挑战外敌入侵兵力下限
	private int lordId; // 领主ID
	private int cd; // 外敌入侵冷却时间（用于财神庙等）
	private int maxReinforceCount;// 同玩家对同领地出征、增援最大上限
	private int errCode;
	private String foreignInvasionDropCfg;// 外敌入侵掉落配置(圣都、名州、重郡取世界等级，其余在本列配置)
	private int troopRate;// 外敌入侵伤兵率
	private int maxReinforceUser; // 恶魔之门援军上限（玩家数）
	private int itemId; // 外敌入侵消耗物品ID
	private int itemCost; // 开启消耗品数量
	private int itemReinforceCost;// 援助消耗品数量
	private int time; // 外敌入侵超时开打时间
	private int attackCountPvP;// pvp进攻方数量上限
	private int defendCountPvP;// pvp防守方数量上限
	private int forceAttack; // PVP是否允许强攻 1允许2不允许
	private String pvpDrop; // pvp掉落
	private int sequence; // 查找列表排序
	private int category;// 查找页卡选项（1圣都；2名州；4特殊；3重郡）
	private int minInitiativeVip; // 主动出征的VIP等级下限
	private String alertTitle;// 弹出框中间提示文字

	private String foreignTitle;// 外敌入侵标题
	private String detailIcon;// 详情界面icon
	private String detailSpec;// 详情界面描述
	private int reinforceType;// 增援类型 （1国家增援，2家族增援）
	private int troopCount;// 野地驻兵数量
	private int heroCount;// 野地驻将数量

	public String getForeignTitle() {
		return foreignTitle;
	}

	public void setForeignTitle(String foreignTitle) {
		this.foreignTitle = foreignTitle;
	}

	public String getDetailIcon() {
		return detailIcon;
	}

	public void setDetailIcon(String detailIcon) {
		this.detailIcon = detailIcon;
	}

	public String getDetailSpec() {
		return detailSpec;
	}

	public void setDetailSpec(String detailSpec) {
		this.detailSpec = detailSpec;
	}

	public String getAlertTitle() {
		return alertTitle;
	}

	public void setAlertTitle(String alertTitle) {
		this.alertTitle = alertTitle;
	}

	public int getCanBePlayerOccupied() {
		return canBePlayerOccupied;
	}

	public void setCanBePlayerOccupied(int canBePlayerOccupied) {
		this.canBePlayerOccupied = canBePlayerOccupied;
	}

	public String getForeignInvasionDropCfg() {
		return foreignInvasionDropCfg;
	}

	public void setForeignInvasionDropCfg(String foreignInvasionDropCfg) {
		this.foreignInvasionDropCfg = foreignInvasionDropCfg;
	}

	public String getPvpDrop() {
		return pvpDrop;
	}

	public void setPvpDrop(String pvpDrop) {
		this.pvpDrop = pvpDrop;
	}

	public int getMinInitiativeVip() {
		return minInitiativeVip;
	}

	public void setMinInitiativeVip(int minInitiativeVip) {
		this.minInitiativeVip = minInitiativeVip;
	}

	public void setBonusDesc(String bonusDesc) {
		this.bonusDesc = bonusDesc;
	}

	public void setCd(int cd) {
		this.cd = cd;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setPropId(int propId) {
		this.propId = propId;
	}

	public int getPropId() {
		return propId;
	}

	public String getBonusDesc() {
		return bonusDesc;
	}

	public int getCd() {
		return cd;
	}

	public String getDesc() {
		return desc;
	}

	public int getLordId() {
		return lordId;
	}

	public void setDefenseBuff(int defenseBuff) {
		this.defenseBuff = defenseBuff;
	}

	public void setScaleDesc(String scaleDesc) {
		this.scaleDesc = scaleDesc;
	}

	public int getDefenseBuff() {
		return defenseBuff;
	}

	public String getScaleDesc() {
		return scaleDesc;
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

	public int getLockTime() {
		return lockTime;
	}

	public void setLockTime(int lockTime) {
		this.lockTime = lockTime;
	}

	public int getMaxReinforceCount() {
		return maxReinforceCount;
	}

	public void setMaxReinforceCount(int maxReinforceCount) {
		this.maxReinforceCount = maxReinforceCount;
	}

	public int getTroopRate() {
		return troopRate;
	}

	public void setTroopRate(int troopRate) {
		this.troopRate = troopRate;
	}

	public int getErrCode() {
		return errCode;
	}

	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}

	public void setLordId(int lordId) {
		this.lordId = lordId;
	}

	public String getEvilDoorName() {
		return evilDoorName;
	}

	public void setEvilDoorName(String evilDoorName) {
		this.evilDoorName = evilDoorName;
	}

	public int getMaxReinforceUser() {
		return maxReinforceUser;
	}

	public void setMaxReinforceUser(int maxReinforceUser) {
		this.maxReinforceUser = maxReinforceUser;
	}

	public int getMinArmCountOpenDoor() {
		return minArmCountOpenDoor;
	}

	public void setMinArmCountOpenDoor(int minArmCountOpenDoor) {
		this.minArmCountOpenDoor = minArmCountOpenDoor;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getItemCost() {
		return itemCost;
	}

	public void setItemCost(int itemCost) {
		this.itemCost = itemCost;
	}

	public int getItemReinforceCost() {
		return itemReinforceCost;
	}

	public void setItemReinforceCost(int itemReinforceCost) {
		this.itemReinforceCost = itemReinforceCost;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getAttackCountPvP() {
		return attackCountPvP;
	}

	public void setAttackCountPvP(int attackCountPvP) {
		this.attackCountPvP = attackCountPvP;
	}

	public int getDefendCountPvP() {
		return defendCountPvP;
	}

	public void setDefendCountPvP(int defendCountPvP) {
		this.defendCountPvP = defendCountPvP;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public int getForceAttack() {
		return forceAttack;
	}

	public void setForceAttack(int forceAttack) {
		this.forceAttack = forceAttack;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public int getCategory() {
		return category;
	}

	public int getReinforceType() {
		return reinforceType;
	}

	public void setReinforceType(int reinforceType) {
		this.reinforceType = reinforceType;
	}

	public int getTroopCount() {
		return troopCount;
	}

	public void setTroopCount(int troopCount) {
		this.troopCount = troopCount;
	}

	public int getHeroCount() {
		return heroCount;
	}

	public void setHeroCount(int heroCount) {
		this.heroCount = heroCount;
	}

	static public HolyPropCfg fromString(String line) {
		HolyPropCfg hp = new HolyPropCfg();
		StringBuilder buf = new StringBuilder(line);
		hp.propId = StringUtil.removeCsvInt(buf);
		hp.name = StringUtil.removeCsv(buf);
		hp.scaleDesc = StringUtil.removeCsv(buf);
		hp.icon = StringUtil.removeCsv(buf);
		hp.evilDoorName = StringUtil.removeCsv(buf);
		hp.desc = StringUtil.removeCsv(buf);
		hp.bonusDesc = StringUtil.removeCsv(buf);
		hp.canBePlayerOccupied = StringUtil.removeCsvInt(buf);
		hp.lockTime = StringUtil.removeCsvInt(buf);
		hp.defenseBuff = StringUtil.removeCsvInt(buf);
		hp.minArmCountOpenDoor = StringUtil.removeCsvInt(buf);
		hp.lordId = StringUtil.removeCsvInt(buf);
		hp.cd = StringUtil.removeCsvInt(buf);
		hp.maxReinforceCount = StringUtil.removeCsvInt(buf);
		hp.errCode = StringUtil.removeCsvInt(buf);
		hp.foreignInvasionDropCfg = StringUtil.removeCsv(buf);
		hp.troopRate = StringUtil.removeCsvInt(buf);
		hp.maxReinforceUser = StringUtil.removeCsvInt(buf) + 1;// 还得加上主战 1 人
		hp.itemId = StringUtil.removeCsvInt(buf);
		hp.itemCost = StringUtil.removeCsvInt(buf);
		hp.itemReinforceCost = StringUtil.removeCsvInt(buf);
		hp.time = StringUtil.removeCsvInt(buf);
		hp.attackCountPvP = StringUtil.removeCsvInt(buf);
		hp.defendCountPvP = StringUtil.removeCsvInt(buf);
		hp.forceAttack = StringUtil.removeCsvInt(buf);
		hp.pvpDrop = StringUtil.removeCsv(buf);
		hp.sequence = StringUtil.removeCsvInt(buf);
		hp.category = StringUtil.removeCsvInt(buf);
		hp.minInitiativeVip = StringUtil.removeCsvInt(buf);
		hp.alertTitle = StringUtil.removeCsv(buf);
		hp.foreignTitle = StringUtil.removeCsv(buf);
		hp.detailIcon = StringUtil.removeCsv(buf);
		hp.detailSpec = StringUtil.removeCsv(buf);
		hp.reinforceType = StringUtil.removeCsvInt(buf);
		StringUtil.removeCsv(buf);
		hp.troopCount = StringUtil.removeCsvInt(buf);
		hp.heroCount = StringUtil.removeCsvInt(buf);
		return hp;
	}
}
