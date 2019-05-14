package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.vikings.sanguo.model.BattleSkillEffect;

public class BattleSkillEffectCache extends LazyLoadCache {

	private static final String FILE_NAME = "battle_skill_effect.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((BattleSkillEffect) obj).getKey();
	}

	@Override
	public Object fromString(String line) {
		return BattleSkillEffect.fromString(line);
	}

	public List<BattleSkillEffect> getBattleSkillEffectsBySkillID(int skillId) {
		List<BattleSkillEffect> battleSkillEffects = new ArrayList<BattleSkillEffect>();
		List<BattleSkillEffect> battleSkillEffects2 = getAll();
		for (BattleSkillEffect battleSkillEffect : battleSkillEffects2) {
			if (battleSkillEffect.getId() == skillId) {
				battleSkillEffects.add(battleSkillEffect);
			}
		}
		return battleSkillEffects;
	}
}
