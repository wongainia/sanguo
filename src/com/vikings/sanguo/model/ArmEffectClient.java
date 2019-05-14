package com.vikings.sanguo.model;

import java.util.List;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.ArmEffectInfo;

public class ArmEffectClient {

	private ArmEffectInfo effect;

	private ReturnInfoClient cost;

	private ReturnInfoClient rmbcost; // 元宝消耗

	private ArmEnhanceProp enhanceProp;

	private TroopProp troopProp;

	@SuppressWarnings("unchecked")
	public ArmEffectClient(ArmEffectInfo effect, int armId) {
		this.effect = effect;
		List<ArmEnhanceProp> ls = CacheMgr.armEnhancePropCache.search(armId);
		for (ArmEnhanceProp ap : ls) {
			if (ap.getPropId() == effect.getId()
					&& ap.getLevel() == effect.getLevel()) {
				enhanceProp = ap;
				break;
			}
		}
		if (enhanceProp == null)
			return;
		cost = new ReturnInfoClient();
		rmbcost = new ReturnInfoClient();

		List<ArmEnhanceCost> ls2 = CacheMgr.armEnhanceCostCache.search(armId);
		for (ArmEnhanceCost ac : ls2) {
			if (ac.getPropId() == effect.getId()
					&& ac.getLevel() == effect.getLevel()) {
				cost.addCfg(ac.getType(), ac.getCostId(), ac.getCostAmt());

				rmbcost.addCfg(ac.getType(), ac.getCostId(), ac.getCostRmb());// 元宝消耗
			}
		}

		try {
			troopProp = (TroopProp) CacheMgr.troopPropCache.get(armId);
		} catch (GameException e) {
			troopProp = new TroopProp();
			e.printStackTrace();
		}
	}

	public boolean isValid() {
		return enhanceProp != null;
	}

	public Integer getId() {
		return effect.getId();
	}

	public Integer getLevel() {
		return effect.getLevel();
	}

	public Integer getExp() {
		return effect.getExp();
	}

	/**
	 * 取属性名
	 * 
	 * @return
	 */
	public String getName() {
		return BattlePropDefine.getPropDesc(effect.getId());
	}

	/**
	 * 本级总阶段值
	 * 
	 * @return
	 */
	public int getExpTotal() {
		return enhanceProp.getTotalExp();
	}

	/**
	 * 本级别的强化值
	 * 
	 * @return
	 */
	public int getEnhanceValue() {
		return enhanceProp.getVaule();
	}

	public int getSequence() {
		return enhanceProp.getSequence();
	}

	/**
	 * 本级别强化消耗
	 * 
	 * @return
	 */
	public ReturnInfoClient getCost() {
		return cost;
	}

	/**
	 * 本级别强化消耗元宝
	 */
	public ReturnInfoClient getRmbCost() {
		return rmbcost;
	}

	public ArmEnhanceProp getEnhanceProp() {
		return enhanceProp;
	}

	public void setEnhanceProp(ArmEnhanceProp enhanceProp) {
		this.enhanceProp = enhanceProp;
	}

	/**
	 * 取兵种初始属性值
	 * 
	 * @return
	 */
	public int getInitValue() {
		switch (effect.getId()) {
		case BattlePropDefine.PROP_LIFE:
			return troopProp.getHp();
		case BattlePropDefine.PROP_ATTACK:
			return troopProp.getAttack();
		case BattlePropDefine.PROP_DEFEND:
			return troopProp.getDefend();
		case BattlePropDefine.PROP_RANGE:
			return troopProp.getRange();
		case BattlePropDefine.PROP_BLOCK:
			return troopProp.getBlock();
		case BattlePropDefine.PROP_DEXTEROUS:
			return troopProp.getDexterous();// 灵巧
		case BattlePropDefine.PROP_SPEED:
			return troopProp.getSpeed();
		case BattlePropDefine.PROP_CRIT:
			return troopProp.getCritRate();// 暴击
		case BattlePropDefine.PROP_ANTICRIT:
			return troopProp.getAntiCrit();// 韧性
			// case ArmPropInfoClient.PROP_CAPACITY:
			// return troopProp.getCapacity();// 负重
			// case ArmPropInfoClient.PROP_CONSUME:
			// return troopProp.getFoodConsume();// 常规粮草消耗
		default:
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	public boolean isMax() {
		List<ArmEnhanceProp> ls = CacheMgr.armEnhancePropCache
				.search(enhanceProp.getArmId());
		for (ArmEnhanceProp ap : ls) {
			if (ap.getPropId() == effect.getId()
					&& ap.getLevel() > effect.getLevel()) {
				return false;
			}
		}
		return true;
	}

	public int getArmId() {
		return troopProp.getId();
	}
}
