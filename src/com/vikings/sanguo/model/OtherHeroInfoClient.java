package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.OtherHeroInfo;

public class OtherHeroInfoClient extends BaseHeroInfoClient implements
		Comparable<OtherHeroInfoClient> {
	private static final long serialVersionUID = -2567784570507869071L;

	public OtherHeroInfoClient() {
	}

	public OtherHeroInfoClient(long id, int heroId, int star, int talent)
			throws GameException {
		super(id, heroId, star, talent);
	}

	private int userId;
	private List<OtherHeroArmPropInfoClient> armPropInfos; // 统兵效果

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public List<OtherHeroArmPropInfoClient> getArmPropInfos() {
		return null == armPropInfos ? new ArrayList<OtherHeroArmPropInfoClient>()
				: armPropInfos;
	}

	public List<HeroArmPropClient> getArmPropClient() {
		List<HeroArmPropClient> ls = new ArrayList<HeroArmPropClient>();
		for (OtherHeroArmPropInfoClient it : getArmPropInfos())
			ls.add(((HeroArmPropClient) it));
		return ls;
	}

	// 武将的总战斗力
	public int getHeroAbility() {
		float temp = 0;
		int[] equipValues = getEquipmentValue();
		if (null != armPropInfos) {
			for (OtherHeroArmPropInfoClient hapic : armPropInfos) {
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

	public void setArmPropInfos(List<OtherHeroArmPropInfoClient> armPropInfos) {
		this.armPropInfos = armPropInfos;
	}

	public static OtherHeroInfoClient convert(OtherHeroInfo info)
			throws GameException {
		if (null == info)
			return null;
		OtherHeroInfoClient ohic = new OtherHeroInfoClient(info.getHero(),
				info.getHeroid(), info.getType(), info.getTalent());
		ohic.setUserId(info.getUserid());
		ohic.setLevel(info.getLevel());
		ohic.setExp(info.getExp());
		ohic.setFiefid(info.getFiefid());
		ohic.setState(ohic.getState());
		ohic.setStamina(info.getStamina());
		ohic.setAttack(info.getAttack());
		ohic.setDefend(info.getDefend());
		ohic.setArmPropInfos(OtherHeroArmPropInfoClient.convert2List(info
				.getArmPropInfosList()));
		ohic.setSkillSlotInfos(HeroSkillSlotInfoClient.convert2List(info
				.getSkillSlotInfosList()));
		ohic.setEquipmentSlotInfos(EquipmentSlotInfoClient.convert2List(
				info.getEquipmentInfosList(), info.getHero()));
		ohic.setFavourInfoClient(HeroFavourInfoClient.convert(info
				.getFavourInfo()));
		return ohic;
	}

	public static OtherHeroInfoClient convert(HeroIdInfoClient hiic, int userId)
			throws GameException {
		if (null == hiic)
			return null;
		return CacheMgr.heroInitCache.getOtherHeroInfoClient(hiic.getId(),
				userId, hiic.getSchema());
	}

	public static List<OtherHeroInfoClient> convert2List(
			List<OtherHeroInfo> infos) throws GameException {
		List<OtherHeroInfoClient> list = new ArrayList<OtherHeroInfoClient>();
		if (null == infos || infos.isEmpty())
			return list;
		for (OtherHeroInfo info : infos) {
			list.add(convert(info));
		}
		return list;
	}

	@Override
	public int compareTo(OtherHeroInfoClient another) {
		if (getTalent() == another.getTalent()) {
			if (heroQuality.getTalent() == another.getHeroQuality().getTalent()) {
				return another.getLevel() - getLevel();
			} else {
				return another.getHeroQuality().getTalent()
						- heroQuality.getTalent();
			}
		} else {
			return another.getTalent() - getTalent();
		}
	}

	// 擅长兵
	public String getOtherHeroArmPropsName() {
		StringBuilder name = new StringBuilder();
		if (null != armPropInfos)
			for (OtherHeroArmPropInfoClient hapic : armPropInfos) {
				HeroTroopName htn = hapic.getHeroTroopName();
				if (null != htn)
					name.append(htn.getSlug()).append("、");
			}
		int index = name.lastIndexOf("、");
		if (index >= 0)
			name.deleteCharAt(index);
		return name.toString();
	}
}
