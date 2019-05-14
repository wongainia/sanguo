package com.vikings.sanguo.ui.window;

import java.util.List;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.model.BonusProp;
import com.vikings.sanguo.ui.adapter.BonusAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.widget.CustomListViewWindow;

public class BonusWindow extends CustomListViewWindow {

	@Override
	protected void init() {
		super.init("荣耀榜");
	}

	private List<BonusProp> getBonus() {
		List<BonusProp> all = CacheMgr.bonusCache.all();
		return all;
	}

	@Override
	protected ObjectAdapter getAdapter() {
		BonusAdapter adapter = new BonusAdapter();
		return adapter;
	}

	@Override
	public void showUI() {
		adapter.notifyDataSetChanged();
		super.showUI();
		if (null != adapter) {
			adapter.clear();
			adapter.addItems(getBonus());
			adapter.notifyDataSetChanged();
		}
	}
}
