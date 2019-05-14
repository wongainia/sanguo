package com.vikings.sanguo.ui.window;

import java.util.List;
import java.util.Collections;

import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.ui.adapter.HeroStrengthenAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomListViewWindow;

public class HeroStrengthenListWindow extends CustomListViewWindow {
	private List<HeroInfoClient> heros;

	@Override
	protected void init() {
		adapter = new HeroStrengthenAdapter();
		super.init("将领强化");
		setContentBelowTitle(R.layout.gradient_msg);
		TextView textView = (TextView) window.findViewById(R.id.gradientMsg);
		textView.setTextSize(14);
		ViewUtil.setText(textView, "请点击选择想强化的武将");
	}

	public void open() {
		heros = getHeroinfos();
		this.doOpen();
	}

	@Override
	public void showUI() {
		adapter.clear();
		adapter.addItems(heros);
		adapter.notifyDataSetChanged();
		dealwithEmptyAdpter();
		super.showUI();
	}

	public List<HeroInfoClient> getHeroinfos() {
		List<HeroInfoClient> heroInfos = Account.heroInfoCache.get();
		Collections.sort(heroInfos);
		return heroInfos;
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return adapter;
	}

	@Override
	protected String getEmptyShowText() {
		return "您的幕府中一名将领都没有<br><br>快去[酒馆]中招募将领吧";
	}
}
