package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class UserVip {

	private byte level; // vip等级

	private int charge; // 最低vip积分

	private int loginBonusId;// 每日登录礼包

	private int exActTimes; // 普通副本额外次数

	private int exActivityTimes;// 活动额外次数

	private String desc;// 开通vip奖励文字描述

	private int heroId;// 显示的武将ID（0为不显示）

	private int questId;// 对应的任务ID

	private int freeArenaCount; // 免费巅峰次数

	private int bloodFreeCount;// 血战免费次数

	private int bloodPokerCount;// 血战翻牌免费次数

	private int favourAddTime;// 宠幸增加时间

	private int forgeRetentionPercentage; // 锻造值最低保留百分比

	private byte pushCountryMsg;// 国家频道推送（0不推送；1推送）

	private String bgImg;// vip列表背景图片

	private String vipSpecialDesc;// vip特权描述

	private int smChargeRate;// 短信积分比率

	private int chargeRate;// 非短信充值积分比率

	public byte getLevel() {
		return level;
	}

	public void setLevel(byte level) {
		this.level = level;
	}

	public int getCharge() {
		return charge;
	}

	public void setCharge(int charge) {
		this.charge = charge;
	}

	public int getExActTimes() {
		return exActTimes;
	}

	public void setExActTimes(int exActTimes) {
		this.exActTimes = exActTimes;
	}

	public int getExActivityTimes() {
		return exActivityTimes;
	}

	public void setExActivityTimes(int exActivityTimes) {
		this.exActivityTimes = exActivityTimes;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public int getHeroId() {
		return heroId;
	}

	public void setQuestId(int questId) {
		this.questId = questId;
	}

	public int getQuestId() {
		return questId;
	}

	public int getFreeArenaCount() {
		return freeArenaCount;
	}

	public void setFreeArenaCount(int freeArenaCount) {
		this.freeArenaCount = freeArenaCount;
	}

	public int getLoginBonusId() {
		return loginBonusId;
	}

	public void setLoginBonusId(int loginBonusId) {
		this.loginBonusId = loginBonusId;
	}

	public int getBloodFreeCount() {
		return bloodFreeCount;
	}

	public void setBloodFreeCount(int bloodFreeCount) {
		this.bloodFreeCount = bloodFreeCount;
	}

	public int getBloodPokerCount() {
		return bloodPokerCount;
	}

	public void setBloodPokerCount(int bloodPokerCount) {
		this.bloodPokerCount = bloodPokerCount;
	}

	public int getFavourAddTime() {
		return favourAddTime;
	}

	public void setFavourAddTime(int favourAddTime) {
		this.favourAddTime = favourAddTime;
	}

	public int getForgeRetentionPercentage() {
		return forgeRetentionPercentage;
	}

	public void setForgeRetentionPercentage(int forgeRetentionPercentage) {
		this.forgeRetentionPercentage = forgeRetentionPercentage;
	}

	public boolean isPushCountryMsg() {
		return pushCountryMsg == 1;
	}

	public byte getPushCountryMsg() {
		return pushCountryMsg;
	}

	public void setPushCountryMsg(byte pushCountryMsg) {
		this.pushCountryMsg = pushCountryMsg;
	}

	public String getBgImg() {
		return bgImg;
	}

	public void setBgImg(String bgImg) {
		this.bgImg = bgImg;
	}

	public String getVipSpecialDesc() {
		return vipSpecialDesc;
	}

	public void setVipSpecialDesc(String vipSpecialDesc) {
		this.vipSpecialDesc = vipSpecialDesc;
	}

	public int getSmChargeRate() {
		return smChargeRate;
	}

	public void setSmChargeRate(int smChargeRate) {
		this.smChargeRate = smChargeRate;
	}

	public int getChargeRate() {
		return chargeRate;
	}

	public void setChargeRate(int chargeRate) {
		this.chargeRate = chargeRate;
	}

	public static UserVip fromString(String line) {
		StringBuilder buf = new StringBuilder(line);
		UserVip vip = new UserVip();
		vip.setLevel(StringUtil.removeCsvByte(buf));
		vip.setCharge(StringUtil.removeCsvInt(buf));
		vip.setLoginBonusId(StringUtil.removeCsvInt(buf));
		vip.setExActTimes(StringUtil.removeCsvInt(buf));
		vip.setExActivityTimes(StringUtil.removeCsvInt(buf));
		vip.setDesc(StringUtil.removeCsv(buf));
		vip.setHeroId(StringUtil.removeCsvInt(buf));
		vip.setQuestId(StringUtil.removeCsvInt(buf));
		StringUtil.removeCsv(buf);
		StringUtil.removeCsv(buf);
		StringUtil.removeCsv(buf);
		vip.setFreeArenaCount(StringUtil.removeCsvInt(buf));
		vip.setBloodFreeCount(StringUtil.removeCsvInt(buf));
		vip.setBloodPokerCount(StringUtil.removeCsvInt(buf));
		vip.setFavourAddTime(StringUtil.removeCsvInt(buf));
		vip.setForgeRetentionPercentage(StringUtil.removeCsvInt(buf));
		vip.setPushCountryMsg(StringUtil.removeCsvByte(buf));
		vip.setBgImg(StringUtil.removeCsv(buf));
		vip.setVipSpecialDesc(StringUtil.removeCsv(buf));
		vip.setSmChargeRate(StringUtil.removeCsvInt(buf));
		vip.setChargeRate(StringUtil.removeCsvInt(buf));
		return vip;
	}
}
