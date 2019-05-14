package com.vikings.sanguo.cache;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.vikings.sanguo.model.BattleSkillEquipment;

public class BattleSkillEquipmentCache extends FileCache {

	private static final String FILE_NAME = "battle_skill_equipment.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((BattleSkillEquipment) obj).getKey();
	}

	@Override
	public Object fromString(String line) {
		return BattleSkillEquipment.fromString(line);
	}

	public int getSuitBattleSkillId(int equipId) {
		int skillId = 0;
		Set<Entry<Integer, BattleSkillEquipment>> set = content.entrySet();
		for (Entry<Integer, BattleSkillEquipment> entry : set) {
			BattleSkillEquipment bse = entry.getValue();
			if (bse.getHeroId() != 0)
				continue;
			if (bse.getEquipId1() == equipId || bse.getEquipId2() == equipId
					|| bse.getEquipId3() == equipId
					|| bse.getEquipId4() == equipId) {
				skillId = bse.getSkillId();
				break;
			}
		}
		return skillId;
	}

	public int getSuitBattleSkillId(int heroId, List<Integer> equipIds) {
		int skillId = 0;
		if (null != equipIds && equipIds.size() >= 4) {
			Set<Entry<Integer, BattleSkillEquipment>> set = content.entrySet();
			// 查找顺序：专属套装、普通套装
			for (Entry<Integer, BattleSkillEquipment> entry : set) {
				BattleSkillEquipment bse = entry.getValue();
				// 专属套装
				if (heroId == bse.getHeroId()
						&& equipIds.contains(bse.getEquipId1())
						&& equipIds.contains(bse.getEquipId2())
						&& equipIds.contains(bse.getEquipId3())
						&& equipIds.contains(bse.getEquipId4())) {
					skillId = bse.getSkillId();
					break;
				}
				// 普通套装
				if (0 == bse.getHeroId()
						&& equipIds.contains(bse.getEquipId1())
						&& equipIds.contains(bse.getEquipId2())
						&& equipIds.contains(bse.getEquipId3())
						&& equipIds.contains(bse.getEquipId4())) {
					skillId = bse.getSkillId();
				}
			}
		}
		return skillId;
	}

}
