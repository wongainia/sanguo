package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.HolyCategory;
import com.vikings.sanguo.model.HolyProp;

public class HolyPropCache extends LazyLoadCache {

	@Override
	public String getName() {
		return "holy_fief.csv";
	}

	@Override
	public Object getKey(Object obj) {
		return ((HolyProp) obj).getFiefId();
	}

	@Override
	public Object fromString(String line) {
		return HolyProp.fromString(line);
	}

	public boolean isHoly(Long fiefId) {
		return getContent().containsKey(fiefId);
	}

	public List<HolyProp> getHolyPropsByCategory(int category) {
		List<HolyProp> holyPropTotal = getAll();
		List<HolyProp> holyProps = new ArrayList<HolyProp>();
		for (HolyProp holyProp : holyPropTotal) {
			if (holyProp.getCategory() == category
					&& !holyProps.contains(holyProp)) {
				if (category == HolyCategory.SHENJI || category == HolyCategory.SHENGDU) {
					// 神迹、圣都不分国家
					holyProps.add(holyProp);
				} else {
					// 重镇  名城 与国家有关
					if (holyProp.getCountry().getCountryId() == Account.user
							.getCountry()) {
						holyProps.add(holyProp);
					}
				}

			}
		}
		return holyProps;
	}

	public int size() {
		return getContent().size();
	}

	public List<Long> getHolyFiefIds() {
		Object[] arrays = getContent().keySet().toArray();
		List<Long> ids = new ArrayList<Long>();
		for (int i = 0; i < arrays.length; i++)
			ids.add((Long) arrays[i]);
		return ids;
	}

	public List<Long> getHolyFiefIds(byte type, int category) {
		List<Long> ids = new ArrayList<Long>();

		List<HolyProp> hps = new ArrayList<HolyProp>();
		for (Object o : getContent().values()) {
			HolyProp h = (HolyProp) o;
			if (h.getType() == type && h.getCategory() == category)
				hps.add(h);
		}

		Collections.sort(hps, new Comparator<HolyProp>() {
			@Override
			public int compare(HolyProp lhs, HolyProp rhs) {
				return lhs.getSequence() - rhs.getSequence();
			}
		});

		for (HolyProp holyProp : hps)
			ids.add(holyProp.getFiefId());

		return ids;
	}

	public List<Integer> getProvinces() {
		List<HolyProp> hps = new ArrayList<HolyProp>();
		for (Object o : getContent().values()) {
			HolyProp h = (HolyProp) o;
			if (h.getType() == 3) // 重鎮
				hps.add(h);
		}

		List<Integer> provinces = new ArrayList<Integer>();
		for (HolyProp it : hps) {
			if (!provinces.contains(it.getProvince()))
				provinces.add(it.getProvince());
		}

		// 排序
		Collections.sort(provinces, new Comparator<Integer>() {
			@Override
			public int compare(Integer lhs, Integer rhs) {
				return lhs - rhs;
			}
		});

		return provinces;
	}

	public List<Long> getTownIdsByProvince(int province) {
		List<Long> ids = new ArrayList<Long>();

		List<HolyProp> hps = new ArrayList<HolyProp>();
		for (Object o : getContent().values()) {
			HolyProp h = (HolyProp) o;
			if (h.getType() == 3 && h.getProvince() == province) // 重鎮
				hps.add(h);
			// ids.add(h.getFiefId());
		}

		Collections.sort(hps, new Comparator<HolyProp>() {
			@Override
			public int compare(HolyProp lhs, HolyProp rhs) {
				return lhs.getSequence() - rhs.getSequence();
			}
		});

		for (HolyProp holyProp : hps)
			ids.add(holyProp.getFiefId());

		return ids;
	}

	public String getFiefName(long fiefId) {
		if (!isHoly(fiefId))
			return "";

		try {
			HolyProp hp = (HolyProp) CacheMgr.holyPropCache.get(fiefId);
			if (1 == hp.getType())
				return "圣都 " + hp.getName();
			else
				return hp.getName();
		} catch (GameException e) {
			return "";
		}
	}
}
