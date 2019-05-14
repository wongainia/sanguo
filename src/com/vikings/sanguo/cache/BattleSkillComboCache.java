package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.vikings.sanguo.model.BattleHeroInfoClient;
import com.vikings.sanguo.model.SkillCombo;
import com.vikings.sanguo.utils.ListUtil;

public class BattleSkillComboCache extends LazyLoadCache {
	private static final String FILE_NAME = "battle_skill_combo.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((SkillCombo) obj).getKey();
	}

	@Override
	public Object fromString(String line) {
		return SkillCombo.fromString(line);
	}

	public SkillCombo getSkillComboByHeroIds(List<BattleHeroInfoClient> bhic) {
		if (ListUtil.isNull(bhic))
			return null;
		List<Integer> ids = new ArrayList<Integer>();
		for (BattleHeroInfoClient battleHeroInfoClient : bhic) {
			if (battleHeroInfoClient.getHeroId() != 0) {
				ids.add(battleHeroInfoClient.getHeroId());
			}
		}

		List<SkillCombo> skillCombos = getAll();
		for (SkillCombo skillCombo : skillCombos) {
			if (skillCombo.getHero1Id() != 0
					&& ids.contains(skillCombo.getHero1Id())
					&& skillCombo.getHero2Id() != 0
					&& ids.contains(skillCombo.getHero2Id())) {
				if (skillCombo.getHero3Id() == 0) {
					return skillCombo;
				} else if (skillCombo.getHero3Id() != 0
						&& ids.contains(skillCombo.getHero3Id())) {
					return skillCombo;
				}
			}
		}

		return null;
	}

	public ArrayList<SkillCombo> getComboHeros(int skillId) {
		Iterator iter = getContent().entrySet().iterator();
		ArrayList<SkillCombo> list = new ArrayList<SkillCombo>();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			SkillCombo val = (SkillCombo) entry.getValue();
			if (skillId == val.getId()) {
				list.add(val);
			}
		}
		return list;
	}
}
