package com.vikings.sanguo.ui.window;

import java.util.Collections;
import java.util.List;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.ui.adapter.HeroAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.widget.CustomListViewWindow;

public class HeroListWindow extends CustomListViewWindow {
	private List<HeroInfoClient> heros;

	@Override
	protected void init() {
		super.init("将领列表");
	}

	public void open() {
		this.doOpen();
	}

	public List<HeroInfoClient> getHeroinfos() {
		List<HeroInfoClient> heroInfos = Account.heroInfoCache.get();
		Collections.sort(heroInfos);
		return heroInfos;
	}

	@Override
	public void showUI() {
		heros = getHeroinfos();
		adapter.clear();
		adapter.addItems(heros);
		adapter.notifyDataSetChanged();
		dealwithEmptyAdpter();
		setRightTxt("将领:" + heros.size() + "/" + Account.user.getHeroLimit());
		super.showUI();
	}
	
	@Override
	protected ObjectAdapter getAdapter() {
		HeroAdapter adapter = new HeroAdapter();
		return adapter;
	}

	@Override
	protected String getEmptyShowText() {
		return "您的幕府中一名将领都没有<br><br>快去[酒馆]中招募将领吧";
	}
}
