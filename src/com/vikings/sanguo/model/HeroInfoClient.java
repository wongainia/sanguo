package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.BaseHeroInfo;
import com.vikings.sanguo.protos.HeroIdInfo;
import com.vikings.sanguo.protos.HeroInfo;
import com.vikings.sanguo.utils.CalcUtil;
import com.vikings.sanguo.utils.StringUtil;

public class HeroInfoClient extends BaseHeroInfoClient implements
		Comparable<HeroInfoClient>, Copyable {

	private static final long serialVersionUID = 3144612956538002958L;

	public HeroInfoClient(long id, int heroId, int star, int talent)
			throws GameException {
		super(id, heroId, star, talent);
	}

	protected HeroInfoClient() {
	}

	public static HeroInfoClient newInstance() {
		return new HeroInfoClient();
	}

	private int staminaResetTime; // 下次体力重置时间
	private int staminaRecoveryCount;// 恢复体力次数
	private List<HeroArmPropInfoClient> armPropInfos; // 统兵效果
	private boolean isChooseAbandon = false;// 是否选择分解

	// 将领数据是有效
	public boolean isValid() {
		return getHeroId() > 0 && heroId > 0;
	}

	public HeroInfoClient setInvalid() {
		this.id = 0;
		this.heroId = 0;
		return this;
	}

	public int getStaminaResetTime() {
		return staminaResetTime;
	}

	public void setStaminaResetTime(int staminaResetTime) {
		this.staminaResetTime = staminaResetTime;
	}

	public int getStaminaRecoveryCount() {
		return staminaRecoveryCount;
	}

	public void setStaminaRecoveryCount(int staminaRecoveryCount) {
		this.staminaRecoveryCount = staminaRecoveryCount;
	}

	@Override
	public int getStamina() { // 到重置时间后，体力恢复满
		if (Config.serverTime() >= getStaminaResetTime() * 1000L) {
			return CacheMgr.heroCommonConfigCache.getMaxStamina();
		} else {
			return super.getStamina();
		}
	}

	public static int getMaxStamina() {
		return CacheMgr.heroCommonConfigCache.getMaxStamina();
	}

	public int getStaminaCount() { // 到重置时间后，体力恢复次数置0
		if (Config.serverTime() >= getStaminaResetTime() * 1000L
				&& null != heroType) {
			return 0;
		} else {
			return getStaminaRecoveryCount();
		}
	}

	// 将领统率值到满的差值
	public int getArmPropValue2Full() {
		int value = 0;
		if (null != armPropInfos && !armPropInfos.isEmpty()) {
			for (HeroArmPropInfoClient hapic : armPropInfos) {
				value += (hapic.getMaxValue() - hapic.getValue());
			}
		}
		return value;
	}

	// 未满的统率属性平均值及个数（0平均值，1个数）
	public int[] getAverageArmProp() {
		int[] values = new int[2];
		if (null != armPropInfos && !armPropInfos.isEmpty()) {
			for (HeroArmPropInfoClient hapic : armPropInfos) {
				if (hapic.getValue() < hapic.getMaxValue()) {
					values[0] += hapic.getValue();
					values[1] += 1;
				}
			}
			if (values[1] > 0)
				values[0] = values[0] / values[1];
		}
		return values;
	}

	// 体力恢复满花费
	public int getRecoverStaminaCost() {
		int rmbPer = CacheMgr.heroCommonConfigCache.getCostRecover100Stamina(); // 100体力需要消耗的元宝数
		int cost = CalcUtil.upNum(CacheMgr.heroCommonConfigCache
				.getMaxStamina() * rmbPer / 100);
		int count = getStaminaRecoveryCount() + 1;
		if (count > CacheMgr.heroCommonConfigCache.getMaxIncreaseCount())
			count = CacheMgr.heroCommonConfigCache.getMaxIncreaseCount();

		cost = cost
				+ CalcUtil.upNum(cost
						* (count - 1)
						* (CacheMgr.heroCommonConfigCache
								.getIncreasePriceRate() / 100f));
		return cost;
	}

	public boolean isStaminaEnough() {
		return getStamina() > CacheMgr.heroCommonConfigCache.getCostStamina();
	}

	// 统兵技能
	public List<HeroArmPropInfoClient> getArmPropInfos() {
		return null == armPropInfos ? new ArrayList<HeroArmPropInfoClient>()
				: armPropInfos;
	}

	public List<HeroArmPropClient> getArmPropClient() {
		List<HeroArmPropClient> ls = new ArrayList<HeroArmPropClient>();
		for (HeroArmPropInfoClient it : getArmPropInfos())
			ls.add(((HeroArmPropClient) it));
		return ls;
	}

	public void setArmPropInfos(List<HeroArmPropInfoClient> armPropInfos) {
		this.armPropInfos = armPropInfos;
	}

	// 武将的总战斗力
	public int getHeroAbility() {
		float temp = 0;
		int[] equipValues = getEquipmentValue();
		if (null != armPropInfos) {
			for (HeroArmPropInfoClient hapic : armPropInfos) {
				List<TroopProp> troopProps = CacheMgr.troopPropCache
						.getTroopPropByType((byte) hapic.getType());
				if (troopProps.size() > 0) {
					TroopProp troopProp = troopProps.get(0);
					temp += hapic.attackAddValue(getRealAttack()
							+ equipValues[0], troopProp) * 100;
					temp += hapic.defendAddValue(getRealDefend()
							+ equipValues[1], troopProp) * 100;
				}

			}
		}
		if (null != skillSlotInfos) {
			for (HeroSkillSlotInfoClient hssic : skillSlotInfos) {
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

	// 返回宠幸技能剩余时间(返回0表示没有宠幸技能)
	public int getFavourSkillLeftTime() {
		int leftTime = 0;
		// if (null != favourInfoClient
		// && BattleSkill.isValidId(favourInfoClient.getSkillid())) {
		// leftTime = favourInfoClient.getTime() - Config.serverTimeSS();
		// }
		if (leftTime < 0)
			leftTime = 0;
		return leftTime;
	}

	public String getEvolveTalentName() {
		String talentName = "";
		if (star < Constants.HERO_MAX_STAR) {
			talentName = getColorTypeName();
		} else {
			byte maxTalent = getHeroProp().getMaxTalent();
			if (talent < maxTalent) {
				try {
					HeroQuality heroQuality = (HeroQuality) CacheMgr.heroQualityCache
							.get((byte) (talent + 1));
					talentName = StringUtil.getHeroTypeName(heroQuality);
				} catch (GameException e) {
					e.printStackTrace();
				}
			}
		}
		return talentName;
	}

	public static HeroInfoClient convert(HeroInfo info) throws GameException {
		if (null == info)
			return null;
		BaseHeroInfo bhi = info.getBi();
		HeroInfoClient hic = new HeroInfoClient(bhi.getId(), bhi.getHeroid(),
				bhi.getType(), bhi.getTalent());
		hic.setLevel(bhi.getLevel());
		hic.setExp(bhi.getExp());
		hic.setFiefid(bhi.getFiefid());
		hic.setStamina(bhi.getStamina());
		hic.setAttack(bhi.getAttack());
		hic.setDefend(bhi.getDefend());
		hic.setTalent(bhi.getTalent());
		hic.setStaminaResetTime(bhi.getStaminaResetTime());
		hic.setStaminaRecoveryCount(bhi.getStaminaRecoveryCount());
		hic.setState(bhi.getState());
		hic.setFavourInfoClient(HeroFavourInfoClient.convert(bhi
				.getFavourInfo()));// 宠幸
		hic.setArmPropInfos(HeroArmPropInfoClient.convert2List(bhi
				.getArmPropInfosList()));
		hic.setSkillSlotInfos(HeroSkillSlotInfoClient.convert2List(bhi
				.getSkillSlotInfosList()));
		hic.setEquipmentSlotInfos(EquipmentSlotInfoClient.convert2List(
				bhi.getEquipmentInfosList(), hic.getId()));
		return hic;
	}

	public static List<HeroInfoClient> convert2List(List<HeroInfo> heroInfos)
			throws GameException {
		List<HeroInfoClient> list = new ArrayList<HeroInfoClient>();
		if (null != heroInfos && !heroInfos.isEmpty()) {
			for (HeroInfo info : heroInfos) {
				list.add(convert(info));
			}
		}
		return list;

	}

	public HeroInfoClient update(HeroInfoClient hic) {
		if (null != hic) {
			setId(hic.getId());
			setHeroId(hic.getHeroId());
			setLevel(hic.getLevel());
			setExp(hic.getExp());
			setFiefid(hic.getFiefid());
			setSkillSlotInfos(hic.getSkillSlotInfos());
			setStamina(hic.getStamina());
			setAttack(hic.getAttack());
			setDefend(hic.getDefend());
			setTalent(hic.getTalent());
			setStaminaResetTime(hic.getStaminaResetTime());
			setArmPropInfos(hic.getArmPropInfos());
			setEquipmentSlotInfos(hic.getEquipmentSlotInfos());
			setHeroProp(hic.getHeroProp());
			setState(hic.getState());
			setStar(hic.getStar());
			setHeroQuality(hic.getHeroQuality());
			setHeroType(hic.getHeroType());
			setStaminaRecoveryCount(hic.getStaminaRecoveryCount());
			setFavourInfoClient(hic.getFavourInfoClient());// 宠幸
		}
		return this;
	}

	public HeroInfoClient copy() {
		HeroInfoClient newHic = new HeroInfoClient();
		newHic.setId(getId());
		newHic.setHeroId(getHeroId());
		newHic.setLevel(getLevel());
		newHic.setExp(getExp());
		newHic.setFiefid(getFiefid());
		List<HeroSkillSlotInfoClient> hssics = new ArrayList<HeroSkillSlotInfoClient>();
		for (HeroSkillSlotInfoClient hssic : getSkillSlotInfos()) {
			hssics.add(hssic.copy());
		}
		newHic.setSkillSlotInfos(hssics);
		newHic.setStamina(getStamina());
		newHic.setAttack(getAttack());
		newHic.setDefend(getDefend());
		newHic.setTalent(getTalent());
		newHic.setStaminaResetTime(getStaminaResetTime());
		List<HeroArmPropInfoClient> hapics = new ArrayList<HeroArmPropInfoClient>();
		for (HeroArmPropInfoClient hapic : getArmPropInfos()) {
			hapics.add(hapic);
		}
		newHic.setArmPropInfos(hapics);
		newHic.setHeroProp(getHeroProp());
		newHic.setState(getState());
		newHic.setStar(getStar());
		newHic.setHeroQuality(getHeroQuality());
		newHic.setHeroType(getHeroType());
		newHic.setStaminaRecoveryCount(getStaminaRecoveryCount());
		newHic.setFavourInfoClient(getFavourInfoClient());// 宠幸
		List<EquipmentSlotInfoClient> esics = new ArrayList<EquipmentSlotInfoClient>();
		for (EquipmentSlotInfoClient esic : getEquipmentSlotInfos()) {
			esics.add(esic);
		}
		newHic.setEquipmentSlotInfos(esics);
		return newHic;
	}

	public void updateAmpPropInfos(List<HeroArmPropClient> hapcs) {
		if (null == hapcs)
			return;
		if (null == armPropInfos) {
			armPropInfos = new ArrayList<HeroArmPropInfoClient>();
		}
		for (HeroArmPropClient hapc : hapcs) {
			boolean has = false;
			for (HeroArmPropInfoClient hapic : armPropInfos) {
				if (hapic.getType() == hapc.getType()) {
					has = true;
					hapic.update(hapc);
					break;
				}
			}
			// if (!has) {
			// HeroArmPropInfoClient hapic = new HeroArmPropInfoClient(
			// hapc.getType());
			// hapic.update(hapc);
			// }
		}

	}

	public String getHeroState() {
		String temp = "";
		switch (state) {
		case HERO_STATE_BATTLE:
			temp = StringUtil.color("作战中", Config.getController()
					.getResourceColorText(R.color.k7_color8));
			break;
		case HERO_STATE_FIEF_DEFENDING:
			temp = StringUtil.color("守城中", Config.getController()
					.getResourceColorText(R.color.k7_color1));
			break;
		case HERO_STATE_STANDBY:
			if (null != Account.manorInfoClient
					&& getFiefid() == Account.manorInfoClient.getPos()) {
				temp = StringUtil.color("待命中", Config.getController()
						.getResourceColorText(R.color.k7_color1));
			} else {
				temp = StringUtil.color("驻扎中", Config.getController()
						.getResourceColorText(R.color.k7_color1));
			}

			break;
		default:
			break;
		}
		return temp;
	}

	public boolean isArmpropFull() {
		boolean isFull = true;
		for (HeroArmPropInfoClient hapic : getArmPropInfos()) {
			if (hapic.getValue() < hapic.getMaxValue()) {
				isFull = false;
				break;
			}
		}
		return isFull;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HeroInfoClient other = (HeroInfoClient) obj;
		return id == other.getId();
	}

	@Override
	public int compareTo(HeroInfoClient another) {
		if (talent == another.getTalent()) {
			if (getStar() == another.getStar()) {
				return another.getLevel() - getLevel();
			} else {
				return another.getStar() - getStar();
			}
		} else {
			return another.getTalent() - talent;
		}
	}

	// 擅长兵
	public String getHeroArmPropsName() {
		StringBuilder name = new StringBuilder();
		if (null != armPropInfos)
			for (HeroArmPropInfoClient hapic : armPropInfos) {
				HeroTroopName htn = hapic.getHeroTroopName();
				if (null != htn)
					name.append(htn.getSlug()).append("、");
			}
		int index = name.lastIndexOf("、");
		if (index >= 0)
			name.deleteCharAt(index);
		return name.toString();
	}

	public String getHeroArmPropsIcon() {
		StringBuilder buf = new StringBuilder();
		if (null != armPropInfos)
			for (HeroArmPropInfoClient hapic : armPropInfos) {
				HeroTroopName htn = hapic.getHeroTroopName();
				if (null != htn)
					buf.append("#").append(htn.getSmallIcon()).append("#");
			}
		return buf.toString();
	}

	public String getSkillTypesIcon() {
		StringBuilder buf = new StringBuilder();
		List<HeroSkillSlotInfoClient> skills = getSkillSlotInfos();
		for (int i = 0; i < skills.size(); i++) {
			HeroSkillSlotInfoClient skill = skills.get(i);
			if (skill.isStaticSkill())
				continue;
			// buf.append("#").append(skill.getMainType().getIcon()).append("#");
		}
		return buf.toString();
	}

	public int getSkillSlotCountExceptStatic() {
		int count = 0;
		for (HeroSkillSlotInfoClient hssic : getSkillSlotInfos()) {
			if (hssic.isStaticSkill())
				continue;
			count++;
		}
		return count;
	}

	public boolean canEvolve() {
		return getLevel() >= getEvolveLevel()
				&& !(isMaxStar() && (isMaxTalent() || isWorldLevelLimit()));
	}

	public boolean canUpgrade() {
		return getLevel() < getEvolveLevel();
	}

	public boolean canStrength() {
		if (null != armPropInfos && !armPropInfos.isEmpty()) {
			for (HeroArmPropInfoClient apic : armPropInfos) {
				if (apic.getValue() < apic.getMaxValue())
					return true;
			}
		}
		return false;
	}

	public String getEvolveLvlDesc() {
		if (canEvolve())
			return getLevel() + "/" + getEvolveLevel();
		else
			return StringUtil.color("" + getLevel(), R.color.k7_color8) + "/"
					+ getEvolveLevel();
	}

	public String getHeroTypeName() {
		return StringUtil.getHeroTypeName(getHeroQuality());
	}

	public String getHeroName() {
		return StringUtil.getHeroName(getHeroProp(), getHeroQuality());
	}

	public List<HeroSkillSlotInfoClient> getStaticSkill() {
		List<HeroSkillSlotInfoClient> staticSkills = new ArrayList<HeroSkillSlotInfoClient>();
		for (HeroSkillSlotInfoClient hssic : getSkillSlotInfos()) {
			if (hssic.isStaticSkill())
				staticSkills.add(hssic);
		}
		return staticSkills;
	}

	@Override
	public void copyFrom(Object another) {
		update((HeroInfoClient) another);
	}

	public boolean needIdentify() {
		return getTalent() >= CacheMgr.dictCache.getDictInt(
				Dict.TYPE_HERO_IDENTIFY, 1);
	}

	public HeroIdInfo toArenaHeroIdInfo(int role) {
		return new HeroIdInfo().setHero(getId()).setHeroid(getHeroId())
				.setRole(role);
	}

	/**
	 * 取将领分解后所能转化的总经验（从talent=1、star=1、level=1开始计算）
	 * 
	 * @return
	 */
	public int getAbandonExp() {
		List<HeroLevelUp> levelUps = CacheMgr.heroLevelUpCache.getAll();
		int total = getExp();
		for (HeroLevelUp levelUp : levelUps) {
			if (levelUp.getTalent() < getTalent()) {
				total += levelUp.getNeedExp();
			} else if (levelUp.getTalent() == getTalent()) {
				if (levelUp.getStar() < getStar()) {
					total += levelUp.getNeedExp();
				} else if (levelUp.getStar() == getStar()
						&& levelUp.getLevel() < getLevel()) {
					total += levelUp.getNeedExp();
				}
			}
		}
		total = (int) (total / 100f * 70);
		return total;
	}

	/**
	 * 取将领分解后所能转化的总统率
	 * 
	 * @return
	 */
	public int getAbandonArmPropValue() {
		int total = 0;
		List<HeroArmPropInfoClient> list = getArmPropInfos();
		for (HeroArmPropInfoClient hapic : list) {
			total += hapic.getValue();
		}
		total = (int) ((total * 1f / list.size() - 15000) / 100f * 80);
		if (total < 0)
			total = 0;
		return total;
	}

	public void setChooseAbandon(boolean isChoose) {
		this.isChooseAbandon = isChoose;
	}

	public boolean getChooseAbandon() {
		return this.isChooseAbandon;
	}
}
