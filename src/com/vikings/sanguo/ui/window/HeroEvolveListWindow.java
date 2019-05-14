package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.adapter.HeroEvolveAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.widget.CustomListViewWindow;

public class HeroEvolveListWindow extends CustomListViewWindow {
	public static CallBack mStep404CallBack;

	@Override
	protected void init() {
		super.init("选择进化将领");
	}

	public void open() {
		this.doOpen();
	}

	@Override
	public void showUI() {
		super.showUI();
		List<HeroInfoClient> heros = getHeroinfos();
		adapter.clear();
		adapter.addItems(heros);
		adapter.notifyDataSetChanged();
		dealwithEmptyAdpter();
	}

	// 是否有可进化的将领
	public static boolean hasCanEvolveHero() {
		List<HeroInfoClient> canEvolveHeros = getHeroinfos();
		return !ListUtil.isNull(canEvolveHeros);
	}

	public static List<HeroInfoClient> getHeroinfos() {
		List<HeroInfoClient> hics = new ArrayList<HeroInfoClient>();
		List<HeroInfoClient> allHeros = Account.heroInfoCache.get();
		for (HeroInfoClient hic : allHeros) {
			if (canEvolve(hic))
				hics.add(hic);
		}
		Collections.sort(hics, new Comparator<HeroInfoClient>() {
			@Override
			public int compare(HeroInfoClient hic1, HeroInfoClient hic2) {
				if (hic1.getTalent() == hic2.getTalent()) {
					if (hic1.getStar() == hic2.getStar()) {
						return hic2.getLevel() - hic1.getLevel();
					} else {
						return hic2.getStar() - hic1.getStar();
					}

				} else {
					return hic2.getTalent() - hic1.getTalent();
				}
			}
		});
		return hics;
	}

	public static boolean canEvolve(HeroInfoClient hic) {
		if (hic.getLevel() < hic.getEvolveLevel())
			return false;
		if (hic.getStar() >= Constants.HERO_MAX_STAR && hic.isMaxTalent())
			return false;
		if (hic.isInBattle())
			return false;
		return true;
	}

	@Override
	protected ObjectAdapter getAdapter() {
		HeroEvolveAdapter adapter = new HeroEvolveAdapter();
		return adapter;
	}

	@Override
	protected String getEmptyShowText() {
		return "当前没有能进化的将领!<br/>只有达到满级的将领才能进化";
	}

}
