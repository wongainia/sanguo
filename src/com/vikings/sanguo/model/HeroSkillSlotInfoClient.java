package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.HeroSkillSlotInfo;

public class HeroSkillSlotInfoClient {
	private int id;
	private int skillId; // 已装备技能id
	private boolean staticSkill;// 是否固化技能
	private BattleSkill battleSkill;

	/**
	 * 此构造方法传入的id必须为有效的id
	 * 
	 * @param skillId
	 * @throws GameException
	 */
	public HeroSkillSlotInfoClient(int skillId) throws GameException {
		this(skillId, true);
	}

	private HeroSkillSlotInfoClient(int skillId, boolean needValidId)
			throws GameException {
		this.skillId = skillId;
		if (BattleSkill.isValidId(skillId)) {
			battleSkill = (BattleSkill) CacheMgr.battleSkillCache.get(skillId);
		} else {
			if (needValidId)
				throw new GameException("无效的技能id");
		}
	}

	public BattleSkill getBattleSkill() {
		return battleSkill;
	}

	public void setBattleSkill(BattleSkill battleSkill) {
		this.battleSkill = battleSkill;
	}

	public void clearBattleSkill() {
		skillId = 0;
		battleSkill = null;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public boolean isStaticSkill() {
		return staticSkill;
	}

	public void setStaticSkill(boolean staticSkill) {
		this.staticSkill = staticSkill;
	}

	public boolean hasSkill() {
		return skillId > 0;
	}

	public static HeroSkillSlotInfoClient convert(HeroSkillSlotInfo info)
			throws GameException {
		if (null == info)
			return null;
		HeroSkillSlotInfoClient hssic = new HeroSkillSlotInfoClient(
				info.getSkillid(), false);
		hssic.setId(info.getId());
		hssic.setStaticSkill(info.getStaticSkill());
		return hssic;
	}

	public static List<HeroSkillSlotInfoClient> convert2List(
			List<HeroSkillSlotInfo> infos) throws GameException {
		List<HeroSkillSlotInfoClient> list = new ArrayList<HeroSkillSlotInfoClient>();
		if (null == infos || infos.isEmpty())
			return list;
		for (HeroSkillSlotInfo info : infos) {
			list.add(HeroSkillSlotInfoClient.convert(info));
		}
		return list;
	}

	public HeroSkillSlotInfoClient copy() {
		try {
			HeroSkillSlotInfoClient newHssic = new HeroSkillSlotInfoClient(
					skillId, false);
			newHssic.setId(id);
			newHssic.setStaticSkill(staticSkill);
			newHssic.setBattleSkill(battleSkill);
			return newHssic;
		} catch (GameException e) {
			Log.e("HeroSkillSlotInfoClient", e.getMessage());
		}
		return null;
	}

	public void addBattleSkill(int skillId, BattleSkill battleSkill) {
		this.skillId = skillId;
		this.battleSkill = battleSkill;
	}
}
