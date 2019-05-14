package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.BattleAnimEffects;

public class BattleAnimEffectCache extends LazyLoadArrayCache
{
	private static final String FILE_NAME = "skill_ani_effect.csv";

	@Override
	public String getName()
	{
		return FILE_NAME;
	}

	@Override
	public Object fromString(String line)
	{
		return BattleAnimEffects.fromString(line);
	}

	// 根据特效id 和受击方方得到对应的一组图片的信息
	public BattleAnimEffects getEffectAnim(int effectId, int beat)
	{		
		return (BattleAnimEffects) search(effectId, beat);
	}

	@Override
	public long getSearchKey1(Object obj)
	{
		return ((BattleAnimEffects) obj).getId();
	}

	@Override
	public long getSearchKey2(Object obj)
	{
		return ((BattleAnimEffects) obj).getDischarge();
	}
}
