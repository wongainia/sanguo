package com.vikings.sanguo.model;

import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.BattleHeroInfo;
import com.vikings.sanguo.protos.HERO_ROLE;

public class BattleHeroInfoClient extends HeroIdBaseInfoClient {

	public BattleHeroInfoClient(long id, int heroId, int star, int talent)
			throws GameException {
		super(id, heroId, star, talent);
	}

	private int userId;
	private int role; // 对应枚举类型enum HERO_ROLE

	private OtherHeroInfoClient heroInfo;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public boolean isMain() {
		return role == HERO_ROLE.HERO_ROLE_ATTACK_MAIN.getNumber()
				|| role == HERO_ROLE.HERO_ROLE_DEFEND_MAIN.getNumber();
	}

	public OtherHeroInfoClient getHeroInfo() {
		return heroInfo == null ? new OtherHeroInfoClient() : heroInfo;
	}

	public void setHeroInfo(OtherHeroInfoClient heroInfo) {
		this.heroInfo = heroInfo;
	}

	public static BattleHeroInfoClient convert(BattleHeroInfo info)
			throws GameException {
		if (null == info)
			return null;
		if (info.getUserid() == 0) { // userID为0说明是npc将领， here的64位Id存的方案号
			HeroInit mHeroInit = (HeroInit) CacheMgr.heroInitCache
					.get((int) (info.getHero().longValue()));
			OtherHeroInfoClient hic = new OtherHeroInfoClient(
					1225588 + info.getHeroid(), mHeroInit.getHeroId(),
					mHeroInit.getStar(), mHeroInit.getTalent());
			hic.setArmPropInfos(mHeroInit.getArmProps());
			hic.setSkillSlotInfos(mHeroInit.getSkills());
			hic.setLevel(mHeroInit.getLevel());
			hic.setAttack(mHeroInit.getAttack());
			hic.setDefend(mHeroInit.getDefend());
			BattleHeroInfoClient battleHeroInfoClient = new BattleHeroInfoClient(
					hic.getId(), hic.getHeroId(), hic.getStar(),
					hic.getTalent());
			battleHeroInfoClient.setUserId(0);
			battleHeroInfoClient.setHeroInfo(hic);
			battleHeroInfoClient.setRole(info.getRole());
			return battleHeroInfoClient;
		} else {
			BattleHeroInfoClient battleHeroInfoClient = new BattleHeroInfoClient(
					info.getHero(), info.getHeroid(), info.getType(),
					info.getTalent());
			battleHeroInfoClient.setRole(info.getRole());
			return battleHeroInfoClient;
		}
	}
}
