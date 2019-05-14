package com.vikings.sanguo.cache;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.HeroInit;
import com.vikings.sanguo.model.OtherHeroInfoClient;

public class HeroInitCache extends FileCache {
	public static String FILE_NAME = "hero_init.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((HeroInit) obj).getBoxId();
	}

	@Override
	public Object fromString(String line) {
		return HeroInit.fromString(line);
	}

	/**
	 * 
	 * @param id
	 *            唯一id
	 * @param boxId
	 *            方案id
	 * @return
	 * @throws GameException
	 */
	public OtherHeroInfoClient getOtherHeroInfoClient(long id, int userId,
			int boxId) throws GameException {
		HeroInit heroInit = (HeroInit) get(boxId);
		OtherHeroInfoClient ohic = new OtherHeroInfoClient(id,
				heroInit.getHeroId(), heroInit.getStar(), heroInit.getTalent());
		ohic.setUserId(userId);
		ohic.setLevel(heroInit.getLevel());
		ohic.setExp(0);
		ohic.setFiefid(0);
		ohic.setState(0);
		ohic.setStamina(CacheMgr.heroCommonConfigCache.getMaxStamina());
		ohic.setAttack(heroInit.getAttack());
		ohic.setDefend(heroInit.getDefend());
		ohic.setArmPropInfos(heroInit.getArmProps());
		ohic.setSkillSlotInfos(heroInit.getSkills());
		ohic.setEquipmentSlotInfos(heroInit.getEquips());
		ohic.setStamina(CacheMgr.heroCommonConfigCache.getMaxStamina());
		return ohic;
	}

	public HeroInfoClient getHeroInfoClient(long id, int userId, int boxId)
			throws GameException {
		HeroInit heroInit = (HeroInit) get(boxId);
		HeroInfoClient hic = new HeroInfoClient(id, heroInit.getHeroId(),
				heroInit.getStar(), heroInit.getTalent());
		hic.setLevel(heroInit.getLevel());
		hic.setExp(0);
		hic.setFiefid(0);
		hic.setState(0);
		hic.setStamina(CacheMgr.heroCommonConfigCache.getMaxStamina());
		hic.setAttack(heroInit.getAttack());
		hic.setDefend(heroInit.getDefend());
		hic.setArmPropInfos(heroInit.getHeroArmProps());
		hic.setSkillSlotInfos(heroInit.getSkills());
		hic.setEquipmentSlotInfos(heroInit.getEquips());
		hic.setStamina(CacheMgr.heroCommonConfigCache.getMaxStamina());
		return hic;
	}

}
