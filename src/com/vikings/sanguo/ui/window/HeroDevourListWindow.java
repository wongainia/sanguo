package com.vikings.sanguo.ui.window;

import java.util.Collections;
import java.util.List;

import android.view.View;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.ui.adapter.HeroDevourAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomListViewWindow;

public class HeroDevourListWindow extends CustomListViewWindow {

	@Override
	protected void init() {
		super.init("武将升级");
		List<HeroInfoClient> heros = getHeroinfos();
		adapter.addItems(heros);
		setContentBelowTitle(R.layout.gradient_msg);
		ViewUtil.setText(window, R.id.gradientMsg, "请点击选择想升级的武将");
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
	}

	public List<HeroInfoClient> getHeroinfos() {
		List<HeroInfoClient> heroInfos = Account.heroInfoCache.get();
		Collections.sort(heroInfos);
		return heroInfos;
	}

	@Override
	protected View getPopupView() {
		return window;
	}

	@Override
	protected void destory() {
		controller.removeContentFullScreen(window);
	}

	@Override
	protected ObjectAdapter getAdapter() {
		adapter = new HeroDevourAdapter();
		return adapter;
	}

}
