package com.vikings.sanguo.model;

import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.EquipEffect;
import com.vikings.sanguo.utils.CalcUtil;

public class EquipEffectClient {
	private int type; // 技能id
	private int minQuality;
	private int exp; // 锻造经验
	private int rewardTime; // 趁热打铁状态结束时间
	private int coolStartTime;// 缓慢冷却开始时间

	private EquipmentForgeEffect equipmentForgeEffect;
	private BattleSkill battleSkill;

	public EquipEffectClient(int effectId) throws GameException {
		this.type = effectId;
		this.equipmentForgeEffect = (EquipmentForgeEffect) CacheMgr.equipmentForgeEffectCache
				.get(effectId);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getMinQuality() {
		return minQuality;
	}

	public void setMinQuality(int minQuality) {
		this.minQuality = minQuality;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getRewardTime() {
		return rewardTime;
	}

	public void setRewardTime(int rewardTime) {
		this.rewardTime = rewardTime;
	}

	public EquipmentForgeEffect getEffect() {
		return equipmentForgeEffect;
	}

	public void setBattleSkill(BattleSkill battleSkill) {
		this.battleSkill = battleSkill;
	}

	public int getCoolStartTime() {
		return coolStartTime;
	}

	public void setCoolStartTime(int coolStartTime) {
		this.coolStartTime = coolStartTime;
	}

	/**
	 * 趁热打铁剩余时间
	 * 
	 * @return
	 */
	public int getHotUpTime() {
		int time = rewardTime - Config.serverTimeSS();
		if (time < 0)
			time = 0;
		return time;
	}

	/**
	 * 当前时间点实际剩余的锻造值
	 * 
	 * @return
	 */
	public int getRealExp() {
		int time = Config.serverTimeSS() - coolStartTime;
		if (time <= 0) {
			return exp;
		} else {
			int temp = exp
					- (time
							/ CacheMgr.equipmentCommonConfigCache
									.getReducePeriod() * CacheMgr.equipmentCommonConfigCache
								.getReduceVaue());
			if (temp < 0)
				temp = 0;
			return temp;
		}
	}

	/**
	 * 缓慢冷却剩余时间 (单位：秒)
	 * 
	 * @return
	 */
	public int getCoolDownTime() {
		int time = 0;
		int temp = Config.serverTimeSS() - coolStartTime;
		if (temp >= 0) {
			// 缓慢冷却总时间-已经过去的时间
			time = CalcUtil.upNum(1f * exp
					/ CacheMgr.equipmentCommonConfigCache.getReduceVaue())
					* CacheMgr.equipmentCommonConfigCache.getReducePeriod()
					- temp;

			if (time < 0)
				time = 0;
		} else {
			time = CalcUtil.upNum(1f * exp
					/ CacheMgr.equipmentCommonConfigCache.getReduceVaue())
					* CacheMgr.equipmentCommonConfigCache.getReducePeriod()
					- temp;
		}
		return time;
	}

	/**
	 * 取锻造后的效果
	 * 
	 * @return
	 */
	public EquipmentForgeEffect getForgeNextLevel() {
		if (EquipmentForgeEffect.isValidId(type) && null != battleSkill) {
			return CacheMgr.equipmentForgeEffectCache
					.getNextLevel(equipmentForgeEffect);
		}
		return null;
	}

	public static EquipEffectClient convert(EquipEffect equipEffect)
			throws GameException {
		if (null == equipEffect
				|| !EquipmentForgeEffect.isValidId(equipEffect.getType()))
			return null;
		EquipEffectClient eec = new EquipEffectClient(equipEffect.getType());
		eec.setMinQuality(equipEffect.getMinQuality());
		eec.setExp(equipEffect.getExp());
		eec.setRewardTime(equipEffect.getRewardTime());
		eec.setCoolStartTime(equipEffect.getCoolStartTime());
		return eec;
	}
}
