package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.utils.ListUtil;

//从OtherHeroInfoClient 和 HeroInfoClient中抽出共有属性
public class BaseHeroInfoClient extends HeroIdBaseInfoClient {

	private static final long serialVersionUID = -3005921850259605444L;

	public static final int HERO_STATE_STANDBY = 0; // 待命
	public static final int HERO_STATE_FIEF_DEFENDING = HERO_STATE_STANDBY + 1; // 守城中
	public static final int HERO_STATE_BATTLE = HERO_STATE_FIEF_DEFENDING + 1; // 作战中

	protected int level;
	protected int exp;
	protected int state;// 将领状态
	protected long fiefid; // 当前所在地（0表示在庄园）
	protected int stamina; // 体力值
	protected List<HeroSkillSlotInfoClient> skillSlotInfos; // 技能信息
	protected List<EquipmentSlotInfoClient> equipmentSlotInfos;// 装备信息
	protected HeroFavourInfoClient favourInfoClient; // 宠幸

	protected int attack; // 基础武力
	protected int defend; // 基础防御

	protected BaseHeroInfoClient() {
		super();
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

	// 是不是套装--是套装用彩色展现装备背景
	public boolean isHeroSuit() {
		return getSuitBattleSkill() != null;
	}

	// 通过英雄Id 查找组合技能
	public List<SkillCombo> getSkillCombos() {
		List<SkillCombo> mSkillCombos = new ArrayList<SkillCombo>();
		List<SkillCombo> mSkillCombos2 = CacheMgr.battleSkillCombo.getAll();
		for (SkillCombo skillCombo : mSkillCombos2) {
			if (heroId == skillCombo.getHero1Id()
					|| heroId == skillCombo.getHero2Id()
					|| heroId == skillCombo.getHero3Id()) {
				mSkillCombos.add(skillCombo);
			}
		}
		List<SkillCombo> sc = new ArrayList<SkillCombo>();
		sc.addAll(mSkillCombos);
		// 移除组合技 重复的 组合技
		for (SkillCombo skillCombo : mSkillCombos) {
			for (SkillCombo skillCombo2 : mSkillCombos) {
				if (skillCombo.getId() == skillCombo2.getId()) {
					if (skillCombo.getKey() > skillCombo2.getKey()) {
						sc.remove(skillCombo);
					}
				}
			}
		}
		return sc;
	}

	// 通过组合技能 获得技能描述
	public static List<BattleSkill> getBattleSkillsBySkillCombos(
			List<SkillCombo> mSkillCombos) {
		if (ListUtil.isNull(mSkillCombos))
			return null;
		List<BattleSkill> bSkills = new ArrayList<BattleSkill>();
		for (SkillCombo skillCombo : mSkillCombos) {
			try {
				bSkills.add((BattleSkill) CacheMgr.battleSkillCache
						.get(skillCombo.getId()));
			} catch (GameException e) {
				Log.e("BaseHeroInfoClient",
						"battleSkill  id:" + skillCombo.getId()
								+ "  not found!");
				e.printStackTrace();
			}
		}
		return bSkills;
	}

	// 装备的属性加成值 第一位为武力，第二位为防护
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

	public BaseHeroInfoClient(long id, int heroId, int star, int talent)
			throws GameException {
		super(id, heroId, star, talent);
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public long getFiefid() {
		return fiefid;
	}

	public void setFiefid(long fiefid) {
		this.fiefid = fiefid;
	}

	public int getStamina() {
		return stamina;
	}

	public void setStamina(int stamina) {
		this.stamina = stamina;
	}

	public List<HeroSkillSlotInfoClient> getSkillSlotInfos() {
		return null == skillSlotInfos ? new ArrayList<HeroSkillSlotInfoClient>()
				: skillSlotInfos;
	}

	public void setSkillSlotInfos(List<HeroSkillSlotInfoClient> skillSlotInfos) {
		this.skillSlotInfos = skillSlotInfos;
	}

	public List<EquipmentSlotInfoClient> getEquipmentSlotInfos() {
		return null == equipmentSlotInfos ? new ArrayList<EquipmentSlotInfoClient>()
				: equipmentSlotInfos;
	}

	public void setEquipmentSlotInfos(
			List<EquipmentSlotInfoClient> equipmentSlotInfos) {
		this.equipmentSlotInfos = equipmentSlotInfos;
	}

	public EquipmentInfoClient getEquipmentInfoClient(long id) {
		for (EquipmentSlotInfoClient esic : equipmentSlotInfos) {
			if (esic.hasEquipment() && esic.getEic().getId() == id)
				return esic.getEic();
		}
		return null;
	}

	public EquipmentSlotInfoClient getEquipmentSlotInfoClient(byte type) {
		for (EquipmentSlotInfoClient esic : equipmentSlotInfos) {
			if (esic.getType() == type)
				return esic;
		}
		return null;
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

	public int getEquipmentAttack() {
		return getEquipmentValue()[0];
	}

	public HeroFavourInfoClient getFavourInfoClient() {
		return favourInfoClient;// 宠幸技能
	}

	public void setFavourInfoClient(HeroFavourInfoClient favourInfoClient) {
		this.favourInfoClient = favourInfoClient;
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

	public int getEquipmentDefend() {
		return getEquipmentValue()[1];
	}

	public void setDefend(int defend) {
		this.defend = defend;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public boolean isInBattle() {
		return state == HERO_STATE_BATTLE;
	}

	public String getStaticSKill() {
		StringBuilder buf = new StringBuilder();
		if (null != skillSlotInfos) {
			for (HeroSkillSlotInfoClient hssic : skillSlotInfos) {
				if (hssic.isStaticSkill() && null != hssic.getBattleSkill())
					buf.append(hssic.getBattleSkill().getName()).append("、");
			}
		}
		int index = buf.lastIndexOf("、");
		if (index >= 0)
			buf.deleteCharAt(index);
		return buf.toString();
	}

	public List<HeroSkillSlotInfoClient> getStaticSkills() {
		List<HeroSkillSlotInfoClient> ls = new ArrayList<HeroSkillSlotInfoClient>();
		if (!ListUtil.isNull(skillSlotInfos)) {
			for (HeroSkillSlotInfoClient hssic : skillSlotInfos) {
				if (hssic.isStaticSkill() && null != hssic.getBattleSkill())
					ls.add(hssic);
			}
		}
		return ls;
	}

	public void abandonSkill(int slotId) {
		if (null != skillSlotInfos) {
			for (HeroSkillSlotInfoClient hssic : skillSlotInfos) {
				if (hssic.getId() == slotId) {
					hssic.clearBattleSkill();
					break;
				}
			}
		}
	}

	public void addSkill(int slotId, int skillId, BattleSkill skill) {
		if (null != skillSlotInfos) {
			for (HeroSkillSlotInfoClient hssic : skillSlotInfos) {
				if (hssic.getId() == slotId) {
					hssic.addBattleSkill(skillId, skill);
					break;
				}
			}
		}
	}

	public HeroSkillSlotInfoClient getHssicBySlotId(int slotId) {
		if (null != skillSlotInfos) {
			for (HeroSkillSlotInfoClient hssic : skillSlotInfos) {
				if (hssic.getId() == slotId) {
					return hssic;
				}
			}
		}
		return null;
	}

	public boolean isValid() {
		return id > 0;
	}

	// 将领是否可以宠幸
	public boolean canFavour() {
		if (null == heroProp)
			return false;
		if (heroProp.isNoClothHero())
			return false;
		if (heroProp.getRating() <= HeroProp.HERO_RATING_MING_JIANG)
			return false;
		if (getStar() == Constants.HERO_MAX_STAR && level == DEFAULT_LEVEL
				&& talent == TALENT_CHANGE_CLOTH)
			return true;
		return false;
	}
}
