package com.vikings.sanguo.ui.alert;

import java.util.ArrayList;
import java.util.List;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.ArmEffectClient;
import com.vikings.sanguo.model.BattleBuff;
import com.vikings.sanguo.model.BattleEffect;
import com.vikings.sanguo.model.BattlePropDefine;
import com.vikings.sanguo.model.BattleSkill;
import com.vikings.sanguo.model.Buff;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.EquipEffectClient;
import com.vikings.sanguo.model.EquipmentEffect;
import com.vikings.sanguo.model.EquipmentForgeEffect;
import com.vikings.sanguo.model.EquipmentInfoClient;
import com.vikings.sanguo.model.EquipmentInsertItemEffect;
import com.vikings.sanguo.model.EquipmentSlotInfoClient;
import com.vikings.sanguo.model.HeroArmPropInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.OtherHeroArmPropInfoClient;
import com.vikings.sanguo.model.OtherHeroInfoClient;
import com.vikings.sanguo.model.TroopProp;
import com.vikings.sanguo.protos.UserTroopEffectInfo;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.utils.CalcUtil;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.TroopDetailUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class TroopDetailTip extends CustomConfirmDialog {

	private TroopProp troopProp;
	private UserTroopEffectInfo troopEffectInfo;
	private int fiefBattleSkillID = 0;// 城防技能ID

	// 多个英雄 加成值计算
	private List<OtherHeroInfoClient> otherHeroInfoClients;
	private List<HeroInfoClient> heroInfoClients;

	private ViewGroup armBuffLayout;
	private List<Buff> buffs;
	private boolean hasBuffs = false;

	public void setBuffs(List<Buff> buffs) {
		this.buffs = buffs;
		hasBuffs = true;
	}

	public TroopDetailTip() {
		super("士兵详情", DEFAULT, true);
	}

	public TroopDetailTip(boolean isTouchClose) {
		super("士兵详情", DEFAULT);
	}

	public void show(TroopProp troopProp, UserTroopEffectInfo troopEffectInfo,
			List<HeroInfoClient> heroInfoClients, int fiefBattleSkillID) {
		this.heroInfoClients = heroInfoClients;
		this.fiefBattleSkillID = fiefBattleSkillID;
		show(troopProp, troopEffectInfo);
	}

	public void show(TroopProp troopProp, UserTroopEffectInfo troopEffectInfo,
			List<OtherHeroInfoClient> otherHeroInfoClients,
			int fiefBattleSkillID, Object object) {
		this.otherHeroInfoClients = otherHeroInfoClients;
		this.fiefBattleSkillID = fiefBattleSkillID;
		show(troopProp, troopEffectInfo);
	}

	public void show(TroopProp troopProp, UserTroopEffectInfo troopEffectInfo,
			List<OtherHeroInfoClient> otherHeroInfoClients,
			int fiefBattleSkillID, Object object, List<Buff> buffs) {
		setBuffs(buffs);
		this.show(troopProp, troopEffectInfo, otherHeroInfoClients,
				fiefBattleSkillID, object);
	}

	public void show(TroopProp troopProp) {
		show(troopProp, null);
	}

	public void show(TroopProp troopProp, UserTroopEffectInfo troopEffectInfo) {
		if (troopProp == null)
			return;
		this.troopProp = troopProp.copy();
		this.troopEffectInfo = troopEffectInfo;
		// 对troopProp 加 强化值 （策划规定强化值 为 基础值 不属于加成值）
		if (troopEffectInfo == null
				|| troopEffectInfo.getUserid() == Account.user.getId()) {
			// troopEffectInfo 为空强化效果取自己的  
			TroopDetailUtil.setTroopEnhanceValue(this.troopProp);
		} else {
			TroopDetailUtil
					.TroopEffectVal(this.troopEffectInfo, this.troopProp);
		}
		adapterArmBuffViews(buffs);
		setValue();
		super.show();
	}

	private void setValue() {
		TroopDetailUtil.setTroopProp(content, troopProp);
		setPropValue();
	}

	private void setPropValue() {
		// 生命
		String hp = getColorExtValue(troopProp.getHp(),
				BattlePropDefine.PROP_LIFE, false);
		ViewUtil.setRichText(content, R.id.hp,
				BattlePropDefine.getPropDesc(BattlePropDefine.PROP_LIFE) + ": "
						+ troopProp.getHp() + hp);

		// 射程
		String range = getColorExtValue(troopProp.getRange(),
				BattlePropDefine.PROP_RANGE, false);
		ViewUtil.setRichText(content, R.id.range,
				BattlePropDefine.getPropDesc(BattlePropDefine.PROP_RANGE)
						+ ": " + troopProp.getRange() + range);

		// 暴击率
		int critRate = (int) getExtValue(troopProp.getCritRate(),
				BattlePropDefine.PROP_CRIT, false);
		ViewUtil.setRichText(content, R.id.crit,
				BattlePropDefine.getPropDesc(BattlePropDefine.PROP_CRIT) + ": "
						+ getCritRateValue(troopProp.getCritRate())
						+ getCritRateDesc(critRate));

		// 韧性
		String antiCrit = getColorExtValue(troopProp.getAntiCrit(),
				BattlePropDefine.PROP_ANTICRIT);
		ViewUtil.setRichText(content, R.id.antiCritTitle,
				BattlePropDefine.getPropDesc(BattlePropDefine.PROP_ANTICRIT)
						+ ": " + troopProp.getAntiCrit() + antiCrit + " ("
						+ getAntiCritRateDesc(troopProp.getAntiCrit())
						+ "免爆率，减免" + getRemit(troopProp.getAntiCrit())
						+ "暴击伤害)");

		// 攻击
		String attack = getColorExtValue(troopProp.getAttack(),
				BattlePropDefine.PROP_ATTACK);
		ViewUtil.setRichText(content, R.id.atk,
				BattlePropDefine.getPropDesc(BattlePropDefine.PROP_ATTACK)
						+ ": " + troopProp.getAttack() + attack);

		// 防御
		String defend = getColorExtValue(troopProp.getDefend(),
				BattlePropDefine.PROP_DEFEND);
		ViewUtil.setRichText(content, R.id.def,
				BattlePropDefine.getPropDesc(BattlePropDefine.PROP_DEFEND)
						+ ": " + troopProp.getDefend() + defend);

		// 暴伤
		int critMultiple = (int) getExtValue(troopProp.getCritMultiple(),
				BattlePropDefine.PROP_CRIT_MULTIPLE, false);
		ViewUtil.setRichText(content, R.id.critTitle,
				"暴伤: " + troopProp.getCritMultiple() + "% "
						+ getCritValue(critMultiple));

		// 出征消耗
		ViewUtil.setRichText(content, R.id.food, "出征消耗: #fief_food#"
				+ getFoodValue(troopProp.getFood()) + "/次", true);

		// 驻防消耗
		ViewUtil.setRichText(content, R.id.foodConsume, "驻防消耗: #fief_food#"
				+ getFoodValue(troopProp.getFoodConsume()) + "/小时", true);
	}

	// 粮草消耗
	private String getFoodValue(int food) {
		float value = food / 1000f;
		if (value > 10) {
			return String.valueOf((int) value);
		} else {
			if (value >= 0.1)
				return CalcUtil.format(value);
			else if (value < 0.1 && value >= 0.01)
				return CalcUtil.format2(value);
			else
				return CalcUtil.format3(value);
		}
	}

	// 三国 暴率直接取配置
	private String getCritRateDesc(int crit) {
		return StringUtil.color(" +" + CalcUtil.format2(crit / 10000f) + "%",
				controller.getResourceColorText(R.color.color19));
	}

	// 暴击率基础值
	private String getCritRateValue(int crit) {
		return CalcUtil.format2(crit / 10000f) + "%";
	}

	// 暴伤加成
	private String getCritValue(int critMultiple) {
		return StringUtil.color(" +" + critMultiple + "%",
				controller.getResourceColorText(R.color.color19));
	}

	// 免暴伤
	private String getRemit(int antiCrit) {
		int param = CacheMgr.dictCache.getDictInt(Dict.TYPE_ARM_PROP_EFFECT, 5);
		float rate = antiCrit / 10000f * param;
		return StringUtil.color(CalcUtil.format2(rate) + "%",
				controller.getResourceColorText(R.color.color19));
	}

	// 爆率（1=0.01%）=韧性
	private String getAntiCritRateDesc(int antiCrit) {
		return StringUtil.color(CalcUtil.format2(antiCrit / 10000f) + "%",
				controller.getResourceColorText(R.color.color19));
	}

	private String getColorExtValue(int baseValue, int propId) {
		return getColorExtValue(baseValue, propId, true);
	}

	/**
	 * @param baseValue
	 * @param propId
	 * @param intValue
	 *            结果是否取整
	 * @return
	 */
	private String getColorExtValue(int baseValue, int propId, boolean intValue) {
		if (intValue)
			return StringUtil.color(
					" +" + (int) (getExtValue(baseValue, propId, intValue)),
					R.color.color19);
		else
			return StringUtil.color(
					" +"
							+ CalcUtil.format2(getExtValue(baseValue, propId,
									intValue)), R.color.color19);
	}

	// 暴率效果加成值 直接提取本级强化配置的加成
	private String getColorBattleLogExtValue(ArmEffectClient aec) {
		return StringUtil.color(" +" + aec.getEnhanceValue(), R.color.color19);
	}

	private float getExtValue(int baseValue, int propId, boolean intValue) {
		float baseAtrrValue = baseValue; // 基础值
		float baseAtrrAtkOrDefend = 0;// 攻防值
		float percentValueForward = 0;// 正向百分比
		float percentValueReverse = 1;// 反向百分比
		float finalValue = 0;// 最终值

		// 处理自己的英雄 相关属性加成
		if (!ListUtil.isNull(heroInfoClients))
			for (HeroInfoClient hic : heroInfoClients) {
				if (!hic.isValid())
					continue;
				// 一.**************熟练兵种英雄的 加成值****************************
				List<HeroArmPropInfoClient> hapics = hic.getArmPropInfos();
				HeroArmPropInfoClient maxHapic = null;
				for (int i = 0; i < hapics.size(); i++) {
					HeroArmPropInfoClient hapic = hapics.get(i);
					// 处理英雄影响的目标熟练兵种
					if (hapic.getType() != troopProp.getType()
							&& TroopProp.GOD_TROOP != troopProp.getType())
						continue;
					if (troopProp.isGodSoldier()) { // 神兵特殊处理(遍历，取最高的一种统率)
						if (null == maxHapic) {
							maxHapic = hapic;
						} else {
							if (maxHapic.getValue() < hapic.getValue()) {
								maxHapic = hapic;
							}
						}
					} else {
						/*
						 * 英雄武力、防御影响士兵攻击防御属性值 公式【 ( (武力值+100)^0.5 *40 +
						 * (当前统率值+100)^0.5 *4 - 440 )/100 * 兵种攻击加成系数/100】 【 (
						 * (防护值+100)^0.5 *40 + (当前统率值+100)^0.5 *4 - 440 )/100 *
						 * 兵种防御加成系数/100】
						 */
						if (propId == BattlePropDefine.PROP_ATTACK) {
							// 攻击加成系数百分比
							// percentValue += TroopDetailUtil.percentValue(hic,
							// hapic, troopProp,
							// EquipmentEffect.EFFECT_TYPE_ATTACK) * 100;
							baseAtrrAtkOrDefend += TroopDetailUtil
									.percentValue(hic, hapic, troopProp,
											EquipmentEffect.EFFECT_TYPE_ATTACK);
						} else if (propId == BattlePropDefine.PROP_DEFEND) {
							// 防守加成系数百分比
							// percentValue += TroopDetailUtil.percentValue(hic,
							// hapic, troopProp,
							// EquipmentEffect.EFFECT_TYPE_DEFEND) * 100;
							baseAtrrAtkOrDefend += TroopDetailUtil
									.percentValue(hic, hapic, troopProp,
											EquipmentEffect.EFFECT_TYPE_DEFEND);
						}

						// 装备信息影响该属性
						List<EquipmentSlotInfoClient> esics = hic
								.getEquipmentSlotInfos();// 将领装备对士兵效果
						for (EquipmentSlotInfoClient esic : esics) {
							if (!esic.hasEquipment())
								continue;
							EquipmentInfoClient eic = esic.getEic();// 装备影响该属性

							if (eic.hasStone()) {// 装备是否镶嵌宝石
								EquipmentInsertItemEffect eiie = null;
								try {
									eiie = (EquipmentInsertItemEffect) CacheMgr.equipmentInsertItemEffectCache
											.get(eic.getSlotItemId());
									if (propId == eiie.getPropId()) {// 宝石影响该属性
										finalValue += eiie.getValue();
									}
								} catch (GameException e) {
									e.printStackTrace();
								}
							}
						}

					}

				}

				// 处理神兵攻防 取熟练度最高的值
				if (maxHapic != null) {
					if (propId == BattlePropDefine.PROP_ATTACK) {
						// 攻击加成系数百分比
						baseAtrrAtkOrDefend += TroopDetailUtil.percentValue(
								hic, maxHapic, troopProp,
								EquipmentEffect.EFFECT_TYPE_ATTACK);
					} else if (propId == BattlePropDefine.PROP_DEFEND) {
						// 防守加成系数百分比
						baseAtrrAtkOrDefend += TroopDetailUtil.percentValue(
								hic, maxHapic, troopProp,
								EquipmentEffect.EFFECT_TYPE_DEFEND);
					}
				}

				// 二.******************技能部分影响的 加成值*********************
				/* 装备特效 对加成值的影响 */
				List<EquipmentSlotInfoClient> esics = hic
						.getEquipmentSlotInfos();
				for (EquipmentSlotInfoClient esic : esics) {
					if (esic.getEic() != null
							&& esic.getEic().getEffect() != null) {
						EquipEffectClient effect = esic.getEic().getEffect();
						try {
							EquipmentForgeEffect equipmentForgeEffect = (EquipmentForgeEffect) CacheMgr.equipmentForgeEffectCache
									.get(effect.getType());
							if (TroopDetailUtil.isContainTargetArm(
									troopProp.getType(),
									equipmentForgeEffect.getValidArm())
									&& propId == equipmentForgeEffect
											.getEffectAtrr()) {
								if (equipmentForgeEffect.getAdditionWay() == 1/* 加基础值 */) {
									baseAtrrValue += equipmentForgeEffect
											.getEffectValue();
								} else if (equipmentForgeEffect
										.getAdditionWay() == 2/* 加百分比 */) {
									if (equipmentForgeEffect.getEffectValue() >= 0) {
										percentValueForward += equipmentForgeEffect
												.getEffectValue();
									} else {
										percentValueReverse = (float) (percentValueReverse
												* (100 + equipmentForgeEffect
														.getEffectValue()) * 0.01);
									}
								} else if (equipmentForgeEffect
										.getAdditionWay() == 3/* 加最终值 */) {
									finalValue += equipmentForgeEffect
											.getEffectValue();
								}
							}
						} catch (GameException e) {
							e.printStackTrace();
						}
					}
				}

				/* 技能 对加成值的影响 */
				List<BattleSkill> battleSkills = new ArrayList<BattleSkill>();
				BattleSkill battleSkill = hic.getSuitBattleSkill();// 套装技能
				if (battleSkill != null
						&& TroopDetailUtil.isContainTargetArm(
								troopProp.getType(),
								battleSkill.getAddedValue())
						&& battleSkill.getMainType() == 8/* 符文技能 */) {
					battleSkills.add(battleSkill);
				}

				for (BattleSkill battleSkill2 : battleSkills) {
					List<BattleEffect> battleEffects = TroopDetailUtil
							.getBattleEffect(battleSkill2);
					for (BattleEffect battleEffect : battleEffects) {
						if (propId == battleEffect.getPara1()) {
							if (battleEffect.getPara2() == 1/* 加基础值 */) {
								baseAtrrValue += battleEffect.getPara3();
							} else if (battleEffect.getPara2() == 2/* 加百分比 */) {
								if (battleEffect.getPara3() >= 0) {
									percentValueForward += battleEffect
											.getPara3();
								} else {
									percentValueReverse = (float) (percentValueReverse
											* (100 + battleEffect.getPara3()) * 0.01);
								}
							} else if (battleEffect.getPara2() == 3/* 加最终值 */) {
								finalValue += battleEffect.getPara3();
							}
						}
					}

				}

			}

		// // 处理其它的英雄 相关属性加成
		if (!ListUtil.isNull(otherHeroInfoClients))
			for (OtherHeroInfoClient otherHeroInfoClient : otherHeroInfoClients) {
				if (!otherHeroInfoClient.isValid())
					continue;
				// 一.**************熟练兵种英雄的 加成值****************************
				List<OtherHeroArmPropInfoClient> ohapics = otherHeroInfoClient
						.getArmPropInfos();
				OtherHeroArmPropInfoClient maxHapic = null;
				for (int i = 0; i < ohapics.size(); i++) {
					OtherHeroArmPropInfoClient ohapic = ohapics.get(i);
					// 处理英雄影响的目标熟练兵种
					if (ohapic.getType() != troopProp.getType()
							&& TroopProp.GOD_TROOP != troopProp.getType())
						continue;
					if (troopProp.isGodSoldier()) { // 神兵特殊处理(遍历，取最高的一种统率)
						if (null == maxHapic) {
							maxHapic = ohapic;
						} else {
							if (maxHapic.getValue() < ohapic.getValue()) {
								maxHapic = ohapic;
							}
						}
					} else {
						/*
						 * 英雄武力、防御影响士兵攻击防御属性值 公式【 ( (武力值+100)^0.5 *40 +
						 * (当前统率值+100)^0.5 *4 - 440 )/100 * 兵种攻击加成系数/100】 【 (
						 * (防护值+100)^0.5 *40 + (当前统率值+100)^0.5 *4 - 440 )/100 *
						 * 兵种防御加成系数/100】
						 */
						if (propId == BattlePropDefine.PROP_ATTACK) {
							// 攻击加成系数百分比
							baseAtrrAtkOrDefend += TroopDetailUtil
									.percentValue(otherHeroInfoClient, ohapic,
											troopProp,
											EquipmentEffect.EFFECT_TYPE_ATTACK);
						} else if (propId == BattlePropDefine.PROP_DEFEND) {
							// 防守加成系数百分比
							baseAtrrAtkOrDefend += TroopDetailUtil
									.percentValue(otherHeroInfoClient, ohapic,
											troopProp,
											EquipmentEffect.EFFECT_TYPE_DEFEND);
						}

						// 装备信息影响该属性
						List<EquipmentSlotInfoClient> esics = otherHeroInfoClient
								.getEquipmentSlotInfos();// 将领装备对士兵效果
						for (EquipmentSlotInfoClient esic : esics) {
							if (!esic.hasEquipment())
								continue;
							EquipmentInfoClient eic = esic.getEic();// 装备影响该属性

							if (eic.hasStone()) {// 装备是否镶嵌宝石
								EquipmentInsertItemEffect eiie = null;
								try {
									eiie = (EquipmentInsertItemEffect) CacheMgr.equipmentInsertItemEffectCache
											.get(eic.getSlotItemId());
									if (propId == eiie.getPropId()) {// 宝石影响该属性
										finalValue += eiie.getValue();
									}
								} catch (GameException e) {
									e.printStackTrace();
								}
							}
						}

					}

				}

				// 处理神兵攻防 取熟练度最高的值
				if (maxHapic != null) {
					if (propId == BattlePropDefine.PROP_ATTACK) {
						// 攻击加成系数百分比
						baseAtrrAtkOrDefend += TroopDetailUtil.percentValue(
								otherHeroInfoClient, maxHapic, troopProp,
								EquipmentEffect.EFFECT_TYPE_ATTACK);
					} else if (propId == BattlePropDefine.PROP_DEFEND) {
						// 防守加成系数百分比
						baseAtrrAtkOrDefend += TroopDetailUtil.percentValue(
								otherHeroInfoClient, maxHapic, troopProp,
								EquipmentEffect.EFFECT_TYPE_DEFEND);
					}
				}

				// 二.******************技能部分影响的 加成值*********************
				/* 装备特效 对加成值的影响 */
				List<EquipmentSlotInfoClient> esics = otherHeroInfoClient
						.getEquipmentSlotInfos();
				for (EquipmentSlotInfoClient esic : esics) {
					if (esic.getEic() != null
							&& esic.getEic().getEffect() != null) {
						EquipEffectClient effect = esic.getEic().getEffect();
						try {
							EquipmentForgeEffect equipmentForgeEffect = (EquipmentForgeEffect) CacheMgr.equipmentForgeEffectCache
									.get(effect.getType());
							if (TroopDetailUtil.isContainTargetArm(
									troopProp.getType(),
									equipmentForgeEffect.getValidArm())
									&& propId == equipmentForgeEffect
											.getEffectAtrr()) {
								if (equipmentForgeEffect.getAdditionWay() == 1/* 加基础值 */) {
									baseAtrrValue += equipmentForgeEffect
											.getEffectValue();
								} else if (equipmentForgeEffect
										.getAdditionWay() == 2/* 加百分比 */) {

									if (equipmentForgeEffect.getEffectValue() >= 0) {
										percentValueForward += equipmentForgeEffect
												.getEffectValue();
									} else {
										percentValueReverse = (float) (percentValueReverse
												* (100 + equipmentForgeEffect
														.getEffectValue()) * 0.01);
									}
								} else if (equipmentForgeEffect
										.getAdditionWay() == 3/* 加最终值 */) {
									finalValue += equipmentForgeEffect
											.getEffectValue();
								}
							}
						} catch (GameException e) {
							e.printStackTrace();
						}
					}
				}

				/* 技能 对加成值的影响 */
				List<BattleSkill> battleSkills = new ArrayList<BattleSkill>();
				BattleSkill battleSkill = otherHeroInfoClient
						.getSuitBattleSkill();// 套装技能
				if (battleSkill != null
						&& TroopDetailUtil.isContainTargetArm(
								troopProp.getType(),
								battleSkill.getAddedValue())
						&& battleSkill.getMainType() == 8/* 符文技能 */) {
					battleSkills.add(battleSkill);
				}

				for (BattleSkill battleSkill2 : battleSkills) {
					List<BattleEffect> battleEffects = TroopDetailUtil
							.getBattleEffect(battleSkill2);
					for (BattleEffect battleEffect : battleEffects) {
						if (propId == battleEffect.getPara1()) {
							if (battleEffect.getPara2() == 1/* 加基础值 */) {
								baseAtrrValue += battleEffect.getPara3();
							} else if (battleEffect.getPara2() == 2/* 加百分比 */) {

								if (battleEffect.getPara3() >= 0) {
									percentValueForward += battleEffect
											.getPara3();
								} else {
									percentValueReverse = (float) (percentValueReverse
											* (100 + battleEffect.getPara3()) * 0.01);
								}
							} else if (battleEffect.getPara2() == 3/* 加最终值 */) {
								finalValue += battleEffect.getPara3();
							}

						}
					}

				}

			}

		// 城防技能
		if (fiefBattleSkillID != 0) {
			BattleSkill battleSkill = null;
			try {
				battleSkill = (BattleSkill) CacheMgr.battleSkillCache
						.get(fiefBattleSkillID);
			} catch (GameException e) {
				e.printStackTrace();
			}
			if (battleSkill != null
					&& TroopDetailUtil.isContainTargetArm(troopProp.getType(),
							battleSkill.getAddedValue())) {
				List<BattleEffect> battleEffects = TroopDetailUtil
						.getBattleEffect(battleSkill);
				for (BattleEffect battleEffect : battleEffects) {
					if (propId == battleEffect.getPara1()) {
						if (battleEffect.getPara2() == 1/* 加基础值 */) {
							baseAtrrValue += battleEffect.getPara3();
						} else if (battleEffect.getPara2() == 2/* 加百分比 */) {
							if (battleEffect.getPara3() >= 0) {
								percentValueForward += battleEffect.getPara3();
							} else {
								percentValueReverse = (float) (percentValueReverse
										* (100 + battleEffect.getPara3()) * 0.01);
							}
						} else if (battleEffect.getPara2() == 3/* 加最终值 */) {
							finalValue += battleEffect.getPara3();
						}

					}
				}

			}

		}

		// 设置基础值
		TroopDetailUtil.setTroopPropAtrValue(propId, (int) baseAtrrValue,
				troopProp);
		/*
		 * 攻击=（基础攻击 + 基础修正）*（1+攻击加成）*（1 + 正向影响）* 负向影响 + 最终修正 防御=（基础防御 +
		 * 基础修正）*（1+防御加成）*（1 + 正向影响）* 负向影响 + 最终修正
		 */
		if (propId == BattlePropDefine.PROP_ATTACK) {
			return (int) ((baseAtrrValue + +troopProp.getAttack())
					* baseAtrrAtkOrDefend * (percentValueForward + 100) * 0.01
					* percentValueReverse + finalValue);

		} else if (propId == BattlePropDefine.PROP_DEFEND) {
			return (int) ((baseAtrrValue + troopProp.getDefend())
					* baseAtrrAtkOrDefend * (100 + percentValueForward) * 0.01
					* percentValueReverse + finalValue);
		}

		if (intValue)
			return (int) (baseAtrrValue * (percentValueForward) * 0.01
					* percentValueReverse + finalValue);
		else
			return (float) (baseAtrrValue * (percentValueForward) * 0.01
					* percentValueReverse + finalValue);
	}

	@Override
	protected View getContent() {
		return controller.inflate(R.layout.alert_troop_detail, tip, false);
	}

	private void adapterArmBuffViews(List<Buff> buffs) {
		if (!hasBuffs)
			return;
		ViewUtil.setVisible(content, R.id.buffLayout);
		armBuffLayout = (ViewGroup) content.findViewById(R.id.armbuffLayout);
		content.findViewById(R.id.closeBtn3).setOnClickListener(closeL);
		if (ListUtil.isNull(buffs)) {
			ViewUtil.setVisible(content, R.id.nobuff);
			return;
		}
		armBuffLayout.removeAllViews();
		ViewUtil.setImage(content, R.id.poto_right,
				ImageUtil.getMirrorBitmapDrawable("potpourri_bg"));
		ViewUtil.setText(content.findViewById(R.id.descBuff),
				troopProp.getName());
		ViewUtil.setGone(content, R.id.nobuff);

		for (int i = 0; i < buffs.size(); i++) {
			ViewGroup viewGroup = (ViewGroup) controller
					.inflate(R.layout.alert_armdetail_buff);
			armBuffLayout.addView(viewGroup);

			Buff bf = buffs.get(i);
			BattleBuff battleBuff = null;
			View buffBg = null;
			try {
				battleBuff = (BattleBuff) CacheMgr.battleBuffCache
						.get(bf.buffId);

				buffBg = viewGroup.findViewById(R.id.armbuff_bg);// buff背景图

				buffBg.setBackgroundResource(battleBuff.isGain() ? R.drawable.arm_buff_bg
						: R.drawable.arm_debuff_bg);

				View imageBuff = viewGroup.findViewById(R.id.armbuff);// buff图片
				new ViewImgScaleCallBack(battleBuff.getIcon(), imageBuff,
						Constants.COMMON_ICON_WIDTH,
						Constants.COMMON_ICON_HEIGHT);

				ViewUtil.setText(viewGroup.findViewById(R.id.armbuff_name),
						StringUtil.getNCharStr(battleBuff.getName(), 4));
			} catch (GameException e) {
				e.printStackTrace();
			}
			buffBg.setTag(battleBuff);
			buffBg.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					TextView tv = (TextView) findViewById(R.id.armDescProp);
					BattleBuff bf = (BattleBuff) v.getTag();
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						if (tv.getVisibility() == View.GONE) {
							ViewUtil.setVisible(tv);
							ViewUtil.setRichText(
									findViewById(R.id.armDescProp),
									StringUtil.color(
											StringUtil.getNCharStr(
													bf.getName(), 4)
													+ " ("
													+ bf.getGainTxt() + ") :",
											R.color.color13)
											+ StringUtil.color(bf.getEffect(),
													R.color.color15));
						}
						break;
					case MotionEvent.ACTION_UP:
						ViewUtil.setGone(tv);
						break;

					default:
						break;
					}
					return true;
				}
			});

			// viewGroup.setOnClickListener(new View.OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// TextView tv = (TextView) findViewById(R.id.armDescProp);
			//
			// BattleBuff bf = (BattleBuff) v.getTag();
			// if (tv.getVisibility() == View.GONE) {
			// ViewUtil.setVisible(tv);
			// ViewUtil.setRichText(
			// findViewById(R.id.armDescProp),
			// StringUtil.color(
			// StringUtil.getNCharStr(bf.getName(), 4)
			// + " (" + bf.getGainTxt()
			// + ") :", R.color.color13)
			// + StringUtil.color(bf.getEffect(),
			// R.color.color15));
			// } else if (tv.getVisibility() == View.VISIBLE) {
			// ViewUtil.setGone(tv);
			// }
			// }
			// });

		}
	}
}
