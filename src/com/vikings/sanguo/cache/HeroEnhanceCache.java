package com.vikings.sanguo.cache;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.vikings.sanguo.model.HeroArmPropInfoClient;
import com.vikings.sanguo.model.HeroEnhance;
import com.vikings.sanguo.model.HeroInfoClient;

public class HeroEnhanceCache extends ArrayFileCache {

	private static final String FILE_NAME = "hero_enhance.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object fromString(String line) {
		return HeroEnhance.fromString(line);
	}

	@Override
	public long getSearchKey1(Object obj) {
		return ((HeroEnhance) obj).getType();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return ((HeroEnhance) obj).getAbove();
	}

	public HeroEnhance getNormal(int curValue) {
		List<HeroEnhance> list = search(HeroEnhance.TYPE_BY_NORMAL);
		for (HeroEnhance he : list) {
			if (he.getAbove() <= curValue && he.getBelow() >= curValue) {
				return he;
			}
		}
		return null;
	}

	public int getCurrencyCost(List<HeroArmPropInfoClient> list) {
		int cost = 0;
		if (null != list && !list.isEmpty()) {
			List<HeroEnhance> enhances = search(HeroEnhance.TYPE_BY_CURRENCY);
			for (HeroArmPropInfoClient hapic : list) {
				for (HeroEnhance enhance : enhances) {
					if (hapic.getValue() >= enhance.getAbove()
							&& hapic.getValue() <= enhance.getBelow()) {
						cost += enhance.getCost();
					}
				}
			}
		}
		return cost;
	}

	public int getOneKeyCost(List<HeroArmPropInfoClient> list) {
		float cost = 0;
		if (null != list && !list.isEmpty()) {
			List<HeroEnhance> enhances = search(HeroEnhance.TYPE_BY_CURRENCY);
			if (!enhances.isEmpty()) {
				Collections.sort(enhances, new Comparator<HeroEnhance>() {

					@Override
					public int compare(HeroEnhance object1, HeroEnhance object2) {
						return object1.getAbove() - object2.getAbove();
					}
				});
			}
			for (HeroArmPropInfoClient hapic : list) {
				int value = hapic.getValue();
				for (HeroEnhance enhance : enhances) {
					int add = (enhance.getMaxAdd() + enhance.getMinAdd()) / 2;
					while (value >= enhance.getAbove()
							&& value <= enhance.getBelow()
							&& value < hapic.getMaxValue()) {
						value += add;
						cost += enhance.getCost();
					}
					if (value >= hapic.getMaxValue())
						break;
				}
			}
		}
		return (int) cost;
	}
}
