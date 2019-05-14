package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.BattleLogHeroInfo;

public class BattleLogHeroInfoClient extends HeroIdBaseInfoClient {
	private static final long serialVersionUID = -7763402714977072373L;

	private int userId;
	private int exp;
	private int level;
	private List<OtherHeroArmPropInfoClient> armPropInfos; // 统兵效果
	private List<HeroSkillSlotInfoClient> skillInfos; // 技能信息
	private List<EquipmentSlotInfoClient> equipmentSlotInfos;// 装备信息
	private HeroFavourInfoClient favourInfoClient; // 宠幸
	private int role; // 角色

	protected int attack; // 基础武力
	protected int defend; // 基础防御

	public BattleLogHeroInfoClient() {
	}

	public BattleLogHeroInfoClient(long id, int heroId, int star, int talent)
			throws GameException {
		super(id, heroId, star, talent);
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getAttack() {
		return attack;
	}

	public int getRealAttack() {
		int add = 0;
		if (null != favourInfoClient
				&& null != favourInfoClient.getHeroFavour())
			add = favourInfoClient.getHeroFavour().getAttackEnhan();
		return add + attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getDefend() {
		return defend;
	}

	public int getRealDefend() {
		int add = 0;
		if (null != favourInfoClient
				&& null != favourInfoClient.getHeroFavour())
			add = favourInfoClient.getHeroFavour().getDefendEnhan();
		return add + defend;
	}

	public void setDefend(int defend) {
		this.defend = defend;
	}

	public HeroFavourInfoClient getFavourInfoClient() {
		return favourInfoClient;
	}

	public void setFavourInfoClient(HeroFavourInfoClient favourInfoClient) {
		this.favourInfoClient = favourInfoClient;
	}

	public List<OtherHeroArmPropInfoClient> getArmPropInfos() {
		return armPropInfos == null ? new ArrayList<OtherHeroArmPropInfoClient>()
				: armPropInfos;
	}

	public List<HeroArmPropClient> getHeroArmPropInfos() {
		if (null == armPropInfos)
			return null;

		List<HeroArmPropClient> ls = new ArrayList<HeroArmPropClient>();
		for (OtherHeroArmPropInfoClient it : armPropInfos) {
			ls.add((HeroArmPropClient) it);
		}
		return ls;
	}

	public void setArmPropInfos(List<OtherHeroArmPropInfoClient> armPropInfos) {
		this.armPropInfos = armPropInfos;
	}

	public List<HeroSkillSlotInfoClient> getSkillInfos() {
		return skillInfos == null ? new ArrayList<HeroSkillSlotInfoClient>()
				: skillInfos;
	}

	public void setSkillInfos(List<HeroSkillSlotInfoClient> skillInfos) {
		this.skillInfos = skillInfos;
	}

	public List<EquipmentSlotInfoClient> getEquipmentSlotInfos() {
		return equipmentSlotInfos == null ? new ArrayList<EquipmentSlotInfoClient>()
				: equipmentSlotInfos;
	}

	public void setEquipmentSlotInfos(
			List<EquipmentSlotInfoClient> equipmentInfos) {
		this.equipmentSlotInfos = equipmentInfos;
	}

	public boolean canEvolve() {
		return getLevel() >= getEvolveLevel()
				&& !(isMaxStar() && (isMaxTalent() || isWorldLevelLimit()));
	}

	public boolean canEvolve(int level) {
		return level >= getEvolveLevel()
				&& !(isMaxStar() && (isMaxTalent() || isWorldLevelLimit()));
	}

	public BattleSkill getSuitBattleSkill() {
		if (getEquipmentSlotInfos().size() < 4) // 装备槽不到4个
			return null;
		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < equipmentSlotInfos.size(); i++) {
			EquipmentSlotInfoClient esic = equipmentSlotInfos.get(i);
			if (esic.hasEquipment() && esic.getEic().getProp().isSuitEq())
				ids.add(esic.getEic().getEquipmentId());
		}
		if (ids.size() < 4) // 未穿齐4件
			return null;
		int skillId = CacheMgr.battleSkillEquipmentCache.getSuitBattleSkillId(
				getHeroId(), ids);
		if (skillId > 0) {
			try {
				return (BattleSkill) CacheMgr.battleSkillCache.get(skillId);
			} catch (GameException e) {
				return null;
			}
		} else
			return null;
	}

	public int[] getEquipmentValue() {
		int[] value = new int[2];
		if (getEquipmentSlotInfos().size() > 0)
			for (EquipmentSlotInfoClient esic : equipmentSlotInfos) {
				if (esic.hasEquipment()) {
					EquipmentInfoClient eic = esic.getEic();
					EquipmentEffect ee = CacheMgr.equipmentEffectCache
							.getEquipmentEffect(eic.getEquipmentId(),
									(byte) eic.getQuality(), eic.getLevel());
					if (null != ee) {
						if (ee.getEffectType() == EquipmentEffect.EFFECT_TYPE_ATTACK)
							value[0] += ee.getEffectValue(eic.getLevel());
						else if (ee.getEffectType() == EquipmentEffect.EFFECT_TYPE_DEFEND)
							value[1] += ee.getEffectValue(eic.getLevel());
					}
				}
			}
		return value;
	}

	public int getHeroAbility() {
		float temp = 0;
		int[] equipValues = getEquipmentValue();
		if (null != armPropInfos) {
			for (OtherHeroArmPropInfoClient hapic : armPropInfos) {
				List<TroopProp> troopProps = CacheMgr.troopPropCache
						.getTroopPropByType((byte) hapic.getType());
				if (troopProps.size() > 0) {
					TroopProp troopProp = troopProps.get(0);
					temp += hapic.attackAddValue(getAttack() + equipValues[0],
							troopProp) * 100;
					temp += hapic.defendAddValue(getDefend() + equipValues[1],
							troopProp) * 100;
				}

			}
		}
		if (null != skillInfos) {
			for (HeroSkillSlotInfoClient hssic : skillInfos) {
				if (hssic.hasSkill()) {
					BattleSkill skill = hssic.getBattleSkill();
					byte skillLevel = skill.getLevel();
					if (skillLevel <= 0)
						skillLevel = 1;
					temp = temp * (skillLevel + 50) / 50 * skill.getRatingAdd();
				}
			}
		}
		return (int) temp;
	}

	// 是不是套装--是套装用彩色展现装备背景
	public boolean isHeroSuit() {
		return getSuitBattleSkill() != null;
	}

	public BaseHeroInfoClient getBaseHeroInfoClient() {
		BaseHeroInfoClient bhic = new BaseHeroInfoClient();
		bhic.setId(getId());
		bhic.setHeroId(getHeroId());
		bhic.setStar(getStar());
		bhic.setTalent(getTalent());
		bhic.setHeroProp(getHeroProp());
		bhic.setHeroQuality(getHeroQuality());
		bhic.setHeroType(getHeroType());
		bhic.setExp(getExp());
		bhic.setLevel(getLevel());
		bhic.setSkillSlotInfos(getSkillInfos());
		bhic.setEquipmentSlotInfos(getEquipmentSlotInfos());
		bhic.setAttack(getAttack());
		bhic.setDefend(getDefend());
		bhic.setFavourInfoClient(getFavourInfoClient());
		return bhic;
	}

	public static BattleLogHeroInfoClient convert(BattleLogHeroInfo info)
			throws GameException {
		if (null == info)
			return null;
		BattleLogHeroInfoClient blhic = new BattleLogHeroInfoClient(
				info.getHero(), info.getHeroid(), info.getType(),
				info.getTalent());
		blhic.setUserId(info.getUserid());
		blhic.setExp(info.getExp());
		blhic.setLevel(info.getLevel());
		blhic.setRole(info.getRole());
		if (info.hasArmPropInfos())
			blhic.setArmPropInfos(OtherHeroArmPropInfoClient.convert2List(info
					.getArmPropInfosList()));
		if (info.hasSkillSlotInfos())
			blhic.setSkillInfos(HeroSkillSlotInfoClient.convert2List(info
					.getSkillSlotInfosList()));
		if (info.hasEquipmentInfos())
			blhic.setEquipmentSlotInfos(EquipmentSlotInfoClient.convert2List(
					info.getEquipmentInfosList(), info.getHero()));
		blhic.setAttack(info.getAttack());
		blhic.setDefend(info.getDefend());
		blhic.setFavourInfoClient(HeroFavourInfoClient.convert(info
				.getFavourInfo()));
		return blhic;
	}

	public static List<BattleLogHeroInfoClient> convert2List(
			List<BattleLogHeroInfo> infos) throws GameException {
		List<BattleLogHeroInfoClient> blhics = new ArrayList<BattleLogHeroInfoClient>();
		if (null != infos && !infos.isEmpty()) {
			for (BattleLogHeroInfo info : infos) {
				blhics.add(convert(info));
			}
		}
		return blhics;
	}

	// 将CampaignHero转成BattleLogHeroInfoClient，用于战斗动画
	public static BattleLogHeroInfoClient convertFrom(HeroInit heroInit,
			int userId) throws GameException {
		BattleLogHeroInfoClient blhic = new BattleLogHeroInfoClient();
		blhic.setId(0);
		blhic.setHeroId(heroInit.getHeroId());
		blhic.setStar(heroInit.getStar());
		blhic.setUserId(userId);
		blhic.setExp(0);
		blhic.setLevel(heroInit.getLevel());
		blhic.setArmPropInfos(heroInit.getArmProps());
		blhic.setSkillInfos(heroInit.getSkills());
		HeroProp heroProp = (HeroProp) CacheMgr.heroPropCache.get(heroInit
				.getHeroId());
		blhic.setHeroProp(heroProp);
		blhic.setHeroQuality((HeroQuality) CacheMgr.heroQualityCache
				.get(heroInit.getTalent()));
		blhic.setFavourInfoClient(HeroFavourInfoClient.convert(null));
		return blhic;
	}

	public static OtherHeroInfoClient convertFrom(BattleLogHeroInfoClient blhic) {
		try {
			OtherHeroInfoClient oheroInfoClient = new OtherHeroInfoClient(
					blhic.getId(), blhic.getHeroId(), blhic.getStar(),
					blhic.getTalent());
			oheroInfoClient.setArmPropInfos(blhic.getArmPropInfos());
			oheroInfoClient
					.setEquipmentSlotInfos(blhic.getEquipmentSlotInfos());
			oheroInfoClient.setAttack(blhic.getAttack());
			oheroInfoClient.setDefend(blhic.getDefend());
			oheroInfoClient.setFavourInfoClient(blhic.getFavourInfoClient());
			return oheroInfoClient;
		} catch (GameException e) {
			e.printStackTrace();
		}
		return null;
	}
}
