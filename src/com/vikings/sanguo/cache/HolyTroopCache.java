package com.vikings.sanguo.cache;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.vikings.sanguo.model.HolyTroop;

public class HolyTroopCache extends LazyLoadArrayCache {

	public List<HolyTroop> getHolyTroop(int propId) {
		List<HolyTroop> holyTroops = search(propId);
		Collections.sort(holyTroops, new Comparator<HolyTroop>() {
			@Override
			public int compare(HolyTroop ht1, HolyTroop ht2) {
				return ht2.getType() - ht1.getType();
			}
		});
		return holyTroops;
	}

	// 得到士兵总数
	public int getSoldierCnt(int propId) {
		int cnt = 0;
		List<HolyTroop> holyTroops = getHolyTroop(propId);
		for (HolyTroop holyTroop : holyTroops) {
			if (HolyTroop.TYPE_ARM == holyTroop.getType()) {
				cnt = cnt + holyTroop.getTroopCnt();
			}
		}
		return cnt;
	}

	// 得到将领总数
	public int getGeneralCnt(int propId) {
		int cnt = 0;
		List<HolyTroop> holyTroops = getHolyTroop(propId);
		for (HolyTroop holyTroop : holyTroops) {
			if (HolyTroop.TYPE_HERO == holyTroop.getType()) {
				cnt = cnt + 1;
			}
		}
		return cnt;
	}

	// // 有主将 则取主将 否则取副将
	// public HeroInit getGeneral(int propId) {
	// HeroInit heroInit = null;
	// List<HolyTroop> holyTroops = getHolyTroop(propId);
	// for (HolyTroop holyTroop : holyTroops) {
	// if (HolyTroop.TYPE_HERO == holyTroop.getType()) {
	// try {
	// heroInit = (HeroInit) CacheMgr.heroInitCache.get(holyTroop
	// .getPropId());
	// if (holyTroop.getTroopCnt() == 0/* 主将 */) {
	// return heroInit;
	// }
	// } catch (GameException e) {
	// Log.e("HolyTroopCache", "heroId" + holyTroop.getPropId()
	// + "not found!");
	// e.printStackTrace();
	// }
	// }
	// }
	// return heroInit;
	// }

	@Override
	public String getName() {
		return "holy_troop.csv";
	}

	@Override
	public Object fromString(String line) {
		return HolyTroop.fromString(line);
	}

	@Override
	public long getSearchKey1(Object obj) {
		return ((HolyTroop) obj).getPropId();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return buildKey(((HolyTroop) obj).getType(),
				((HolyTroop) obj).getTroopId());
	}
}
