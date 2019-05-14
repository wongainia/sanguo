package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.model.BattleEffect;

public class BattleEffectCache extends LazyLoadCache {

	private static final String FILE_NAME = "battle_effect.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((BattleEffect) obj).getKey();
	}

	@Override
	public Object fromString(String line) {
		return BattleEffect.fromString(line);
	}

	public List<BattleEffect> getBattleEffectsById(int effectID) {
		List<BattleEffect> battleEffects = new ArrayList<BattleEffect>();
		List<BattleEffect> battleEffects2 = getAll();
		for (BattleEffect battleEffect : battleEffects2) {
			if (battleEffect.getId() == effectID) {
				battleEffects.add(battleEffect);
			}
		}
		return battleEffects;
	}
}
