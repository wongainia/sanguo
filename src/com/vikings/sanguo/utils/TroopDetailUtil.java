package com.vikings.sanguo.utils;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.view.View;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.ArmEffectClient;
import com.vikings.sanguo.model.ArmPropInfoClient;
import com.vikings.sanguo.model.BaseHeroInfoClient;
import com.vikings.sanguo.model.BattleBuffEffect;
import com.vikings.sanguo.model.BattleEffect;
import com.vikings.sanguo.model.BattlePropDefine;
import com.vikings.sanguo.model.BattleSkill;
import com.vikings.sanguo.model.BattleSkillEffect;
import com.vikings.sanguo.model.EquipmentEffect;
import com.vikings.sanguo.model.EquipmentInfoClient;
import com.vikings.sanguo.model.EquipmentSlotInfoClient;
import com.vikings.sanguo.model.OtherHeroArmPropInfoClient;
import com.vikings.sanguo.model.PropTroopName;
import com.vikings.sanguo.model.TroopProp;
import com.vikings.sanguo.protos.TroopEffectInfo;
import com.vikings.sanguo.protos.UserTroopEffectInfo;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;

public class TroopDetailUtil {

	public static void setTroopProp(View content, TroopProp troopProp) {
		new ViewImgScaleCallBack(troopProp.getIcon(),
				content.findViewById(R.id.icon), Constants.COMMON_ICON_WIDTH,
				Constants.COMMON_ICON_HEIGHT);
		ViewUtil.setText(content, R.id.troopName, troopProp.getName());

		try {
			PropTroopName ptn = (PropTroopName) CacheMgr.propTroopNameCache
					.get((int) troopProp.getType());
			ViewUtil.setRichText(content, R.id.troopType, ptn.getName(), true);

			ViewUtil.setImage(content, R.id.armicon, troopProp.getSmallIcon());
		} catch (GameException e) {
			Log.e("TroopDetailTip", e.getErrorMsg());
		}

		ViewUtil.setText(content, R.id.desc, troopProp.getDesc());
	}

	// 士兵强化后增加的值是基础值 “属性：基础值+加成值”
	public static void setTroopEnhanceValue(TroopProp troopProp) {
		ArmPropInfoClient armPropInfoClient = Account.armEnhanceCache
				.getPropByArmId(troopProp.getId());

		List<ArmEffectClient> aecs = armPropInfoClient.getEnhanceList();

		for (int i = 0; i < aecs.size(); i++) {
			ArmEffectClient aec = aecs.get(i);
			setTroopPropEnhanceAtrValue(aec.getEnhanceProp().getPropId(),
					aec.getEnhanceValue(), troopProp);
		}
	}

	// 士兵属性值 + 强化值
	public static void setTroopPropEnhanceAtrValue(int propId, int value,
			TroopProp troop) {
		switch (propId) {
		case BattlePropDefine.PROP_LIFE:// 生命值
			troop.setHp(value + troop.getHp());
			break;
		case BattlePropDefine.PROP_ATTACK:// 攻击
			troop.setAttack(value + troop.getAttack());
			break;

		case BattlePropDefine.PROP_DEFEND:// 防御
			troop.setDefend(value + troop.getDefend());
			break;

		case BattlePropDefine.PROP_RANGE:// 射程
			troop.setRange(value + troop.getRange());
			break;

		case BattlePropDefine.PROP_BLOCK:// 拦截
			troop.setBlock(value + troop.getBlock());
			break;

		case BattlePropDefine.PROP_DEXTEROUS:// 灵巧
			troop.setDexterous(value + troop.getDexterous());
			break;

		case BattlePropDefine.PROP_SPEED:// 速度
			troop.setSpeed(value + troop.getSpeed());
			break;

		case BattlePropDefine.PROP_CRIT:// 暴率
			troop.setCritRate(value + troop.getCritRate());
			break;

		case BattlePropDefine.PROP_CRIT_MULTIPLE:// 暴伤
			troop.setCritMultiple(value + troop.getCritMultiple());
			break;

		case BattlePropDefine.PROP_ANTICRIT:// 韧性
			troop.setAntiCrit(value + troop.getAntiCrit());
			break;
		}
	}

	// 强化值 加到基础属性里面
	public static void TroopEffectVal(UserTroopEffectInfo troopEffectInfo,
			TroopProp troopProp) {
		if (null != troopEffectInfo) {
			for (TroopEffectInfo tei : troopEffectInfo.getInfosList()) {
				if (tei.getArmid() == troopProp.getId())
					setTroopPropEnhanceAtrValue(tei.getAttr().intValue(), tei
							.getValue().intValue(), troopProp);
			}
		}
	}

	// 士兵属性值
	public static void setTroopPropAtrValue(int propID, int value,
			TroopProp troop) {

		switch (propID) {
		case BattlePropDefine.PROP_LIFE:// 生命值
			troop.setHp(value);
			break;
		case BattlePropDefine.PROP_ATTACK:// 攻击
			troop.setAttack(value);
			break;

		case BattlePropDefine.PROP_DEFEND:// 防御
			troop.setDefend(value);
			break;

		case BattlePropDefine.PROP_RANGE:// 射程
			troop.setRange(value);
			break;

		case BattlePropDefine.PROP_BLOCK:// 拦截
			troop.setBlock(value);
			break;

		case BattlePropDefine.PROP_DEXTEROUS:// 灵巧
			troop.setDexterous(value);
			break;

		case BattlePropDefine.PROP_SPEED:// 速度
			troop.setSpeed(value);
			break;

		case BattlePropDefine.PROP_CRIT:// 暴率
			troop.setCritRate(value);
			break;

		case BattlePropDefine.PROP_CRIT_MULTIPLE:// 暴伤
			troop.setCritMultiple(value);
			break;

		case BattlePropDefine.PROP_ANTICRIT:// 韧性
			troop.setAntiCrit(value);
			break;
		}

	}

	// 暴率
	public static String getCritRareDesc(int crit) {
		return CalcUtil.format2(crit / 10000f) + "%";
	}

	// 暴伤
	public static String getCrit(int critMultiple) {
		return critMultiple + "%";
	}

	// 装备的攻防属性加成值
	public static int getEquipmentValue(BaseHeroInfoClient heroInfoClient,
			int attackOrDefend) {
		if (heroInfoClient == null)
			return 0;
		int value = 0;
		List<EquipmentSlotInfoClient> esics = heroInfoClient
				.getEquipmentSlotInfos();
		for (EquipmentSlotInfoClient esic : esics) {
			if (esic.hasEquipment()) {
				EquipmentInfoClient eic = esic.getEic();
				EquipmentEffect ee = CacheMgr.equipmentEffectCache
						.getEquipmentEffect(eic.getEquipmentId(),
								(byte) eic.getQuality(), eic.getLevel());
				if (null != ee) {
					if (ee.getEffectType() == attackOrDefend)
						value += ee.getEffectValue(eic.getLevel());
					else if (ee.getEffectType() == attackOrDefend)
						value += ee.getEffectValue(eic.getLevel());
				}
			}
		}
		return value;
	}

	/*
	 * 
	 * 攻击加成=（熟练度 + 英雄攻击*45）* 士兵攻击系数 / 1000000 防御加成=（熟练度 + 英雄防御*45）* 士兵防御系数
	 * /1000000
	 * 
	 * 攻击=（基础攻击*（1+攻击加成） + 基础修正）*（1 + 正向影响）* 负向影响 + 最终修正 防御=（基础防御*（1+防御加成） +
	 * 基础修正）*（1 + 正向影响）* 负向影响 + 最终修正
	 */
	public static float percentValue(BaseHeroInfoClient hic,
			OtherHeroArmPropInfoClient hapic, TroopProp troopProp,
			int attackOrDefend) {
		int[] equipValues = hic.getEquipmentValue();
		if (EquipmentEffect.EFFECT_TYPE_ATTACK == attackOrDefend) {
			return (float) (1 + ((hapic.getValue() + (hic.getRealAttack() + equipValues[0]) * 45)
					* troopProp.getAtkModulus() * 0.000001));
		}
		/*
		 * 英雄武力、防御影响士兵攻击防御属性值 公式【 ( (武力值+100)^0.5 *40 + (当前统率值+100)^0.5 *4 - 440
		 * )/100 * 兵种攻击加成系数/100】
		 */
		else if (EquipmentEffect.EFFECT_TYPE_DEFEND == attackOrDefend) {
			return (float) (1 + ((hapic.getValue() + (hic.getRealDefend() + equipValues[1]) * 45)
					* troopProp.getDefModulus() * 0.000001));
		}

		return 0;

	}

	public static boolean isContainTargetArm(int target, int armProp) {
		String armPropStr = Integer.toBinaryString(armProp);
		if (target > 0 && target <= armPropStr.length()) {
			char char1 = armPropStr.charAt(armPropStr.length() - target);
			if (char1 == '1') {
				return true;
			}
		}
		return false;
	}

	// 一个技能 对应多个 buffID 一个buffId对应多个效果ID 一个效果ID 对多个属性值加成
	public static List<BattleEffect> getBattleEffect(BattleSkill battleSkill) {
		List<BattleEffect> battleEffec = new ArrayList<BattleEffect>();
		if (battleSkill == null)
			return battleEffec;

		List<BattleSkillEffect> battleSkillEffects = CacheMgr.battleSkillEffectCache
				.getBattleSkillEffectsBySkillID(battleSkill.getId());
		for (BattleSkillEffect battleSkillEffect : battleSkillEffects) {

			List<BattleEffect> battleEffects = CacheMgr.battleEffectCache
					.getBattleEffectsById(battleSkillEffect.getEffectId());
			for (BattleEffect battleEffect : battleEffects) {

				if (battleEffect.getEffectFormula() == 7/* 直接可以通过公式计算 */) {
					battleEffec.add(battleEffect);

				} else if (battleEffect.getEffectFormula() == 6/*
																 * 通过buffId
																 * 再找效果ID
																 */) {

					List<BattleBuffEffect> battleBuffEffects = CacheMgr.battleBuffEffectCache
							.getBattleBuffEffectsById(battleEffect.getPara1());
					for (BattleBuffEffect battleBuffEffect : battleBuffEffects) {
						List<BattleEffect> battleEffects1 = CacheMgr.battleEffectCache
								.getBattleEffectsById(battleBuffEffect
										.getEffectId());
						for (BattleEffect battleEffect1 : battleEffects1) {
							if (battleEffect1.getEffectFormula() == 7) {
								battleEffec.add(battleEffect1);
							}
						}

					}

				}

			}

		}

		return battleEffec;
	}
}
