package com.vikings.sanguo.cache;

import java.util.List;

import com.vikings.sanguo.model.BuildingEffect;
import com.vikings.sanguo.model.BuildingProp;
import com.vikings.sanguo.utils.TroopUtil;

/**
 * 
 * @author susong
 * 
 */
public class BuildingEffectCache extends LazyLoadArrayCache {
	private static final String FILE_NAME = "building_effect.csv";

	public static final int EFFECT_POP_LIMIT = 51;
	public static final int EFFECT_TROOP_LIMIT = 53;
	public static final int EFFECT_STORE_WEIGHT = 54;
	public static final int EFFECT_PIT_WEIGHT = 55;
	public static final int EFFECT_RES_LIMIT = 56;
	public static final int EFFECT_SCALE = 57;
	public static final int EFFECT_REVIVE = 59;
	public static final int EFFECt_ARM_HP_ADD = 60;
	public static final int EFFECT_REVIVE_BOSS = 61;
	public static final int EFFECT_WALL_SKILL = 101;
	public static final int EFFECT_POP_GROW = 501;

	@Override
	public Object fromString(String line) {
		return BuildingEffect.fromString(line);
	}

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public long getSearchKey1(Object obj) {
		return ((BuildingEffect) obj).getBuildingId();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return ((BuildingEffect) obj).getEffectId();
	}

	@SuppressWarnings("unchecked")
	public int getEffectValue(int effectId, int buildingId) {
		List<BuildingEffect> ls = this.search(buildingId);
		for (BuildingEffect e : ls) {
			if (e.getEffectId() == effectId)
				return e.getValue();
		}
		return 0;
	}

	public int getScaleValue(int buildingId) {
		return getEffectValue(EFFECT_SCALE, buildingId);
	}

	public int getBattleSkillId(int buildingId) {
		return getEffectValue(EFFECT_WALL_SKILL, buildingId);
	}

	public int getReviveValue(int buildingId,int index){
		int reviveValue=0;
		if(0==index){
			reviveValue=getEffectValue(EFFECT_REVIVE, buildingId);
		}else if(1==index){
			reviveValue=getEffectValue(EFFECT_REVIVE_BOSS, buildingId);
		}
		return reviveValue;
	}
	
	public int[] getReviveCount(BuildingProp bp,int index) {
		int[] cnt = new int[2];//[0]部队信息    [1]相应部队上限
		if(0==index){
			cnt[0] = TroopUtil.countArm(Account.myLordInfo.getReviveInfo());
		}else if(1==index){
			cnt[0] = TroopUtil.countArm(Account.myLordInfo.getReviveBossInfo());
		}
		cnt[1] = CacheMgr.buildingEffectCache.getReviveValue(bp.getId(),index);
		return cnt;
	}
}
