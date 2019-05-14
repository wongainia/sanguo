package com.vikings.sanguo.model;

import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.utils.StringUtil;

//装备初始化定义
public class EquipmentInit {
	private String desc = "购买后随机获得一种特效";
	private int scheme;// 方案号
	private int id;// 装备id
	private int initLevel;// 初始等级
	private byte initQuality;// 初始品质
	private int extraEffectId;// 附加效果随机方案号
	private byte extraMinQuality;// 附加效果开启最低品质
	private int money;// 金币买入单价
	private int currency;// 元宝买入单价
	private int effectId;// 装备特效id（-1:随机；其他数值为对应技能)

	public int getScheme() {
		return scheme;
	}

	public void setScheme(int scheme) {
		this.scheme = scheme;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getInitLevel() {
		return initLevel;
	}

	public void setInitLevel(int initLevel) {
		this.initLevel = initLevel;
	}

	public byte getInitQuality() {
		return initQuality;
	}

	public void setInitQuality(byte initQuality) {
		this.initQuality = initQuality;
	}

	public int getExtraEffectId() {
		return extraEffectId;
	}

	public void setExtraEffectId(int extraEffectId) {
		this.extraEffectId = extraEffectId;
	}

	public byte getExtraMinQuality() {
		return extraMinQuality;
	}

	public void setExtraMinQuality(byte extraMinQuality) {
		this.extraMinQuality = extraMinQuality;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getCurrency() {
		return currency;
	}

	public void setCurrency(int currency) {
		this.currency = currency;
	}

	public boolean isCurrency() {
		return currency > 0;
	}

	public int getPrice() {
		return isCurrency() ? getCurrency() : getMoney();
	}

	public int getDiscountPrice(int discount) {
		return getPrice() * discount / 100;
	}

	public String getPreIcon() {
		return isCurrency() ? "#rmb#" : "#money#";
	}

	public int getEffectId() {
		return effectId;
	}

	public void setEffectId(int effectId) {
		this.effectId = effectId;
	}

	public boolean isEnoughToBuy(int discount, int count) {
		if (isCurrency()) {
			if (Account.user.getCurrency() >= getDiscountPrice(discount)
					* count)
				return true;
		} else {
			if (Account.user.getMoney() >= getDiscountPrice(discount) * count)
				return true;

		}
		return false;
	}

	// 只在商店界面，没有生成装备的地方调用
	public String getEquipmentEffectDesc() {
		if (effectId == -1)
			return desc;
		try {
			BattleSkill skill = (BattleSkill) CacheMgr.battleSkillCache
					.get(effectId);
			return skill.getEffectDesc();
		} catch (GameException e) {
			return desc;
		}
	}

	public static EquipmentInit fromString(String line) {
		EquipmentInit equipmentInit = new EquipmentInit();
		StringBuilder buf = new StringBuilder(line);
		equipmentInit.setScheme(StringUtil.removeCsvInt(buf));
		equipmentInit.setId(StringUtil.removeCsvInt(buf));
		equipmentInit.setInitLevel(StringUtil.removeCsvInt(buf));
		equipmentInit.setInitQuality(StringUtil.removeCsvByte(buf));
		equipmentInit.setExtraEffectId(StringUtil.removeCsvInt(buf));
		equipmentInit.setExtraMinQuality(StringUtil.removeCsvByte(buf));
		equipmentInit.setMoney(StringUtil.removeCsvInt(buf));
		equipmentInit.setCurrency(StringUtil.removeCsvInt(buf));
		equipmentInit.setEffectId(StringUtil.removeCsvInt(buf));
		return equipmentInit;
	}
}
