package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.model.BattleBuffEffect;

public class BattleBuffEffectCache extends LazyLoadCache {

	private static final String FILE_NAME = "battle_buff_effect.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((BattleBuffEffect) obj).getKey();
	}

	@Override
	public Object fromString(String line) {
		return BattleBuffEffect.fromString(line);
	}

	public List<BattleBuffEffect> getBattleBuffEffectsById(int buffId) {
		List<BattleBuffEffect> battleBuffEffects = new ArrayList<BattleBuffEffect>();
		List<BattleBuffEffect> battleBuffEffects2 = getAll();

		for (BattleBuffEffect battleBuffEffect : battleBuffEffects2) {
			if (battleBuffEffect.getId() == buffId) {
				battleBuffEffects.add(battleBuffEffect);
			}
		}
		return battleBuffEffects;
	}
}
