package com.vikings.sanguo.ui.window;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.vikings.sanguo.model.GuildUserData;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.adapter.SearchResultAdapter;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.widget.CustomBaseListWindow;

public abstract class SearchResultWindow extends CustomBaseListWindow {

	@Override
	protected void init() {
		adapter = new SearchResultAdapter();
		super.init("查找结果");
	}

	public void open() {
		doOpen();
		firstPage();
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return adapter;
	}

	@Override
	public void handleItem(Object o) {
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void updateUI() {
		List<GuildUserData> ls = resultPage.getResult();
		int size = 0;
		if (ls != null && ls.size() != 0) {
			size = ls.size();
			ListUtil.deleteRepeat(ls, adapter.getContent());
			sort(ls);
			adapter.addItems(ls);
			adapter.notifyDataSetChanged();
		}
		resultPage.addIndex(resultPage.getPageSize());
		resultPage.clearResult();
		if (size < resultPage.getPageSize()) {
			resultPage.setTotal(resultPage.getCurIndex());
		} else {
			resultPage.setTotal(Integer.MAX_VALUE);
		}
		dealwithEmptyAdpter();
	}

	@Override
	protected String getEmptyShowText() {
		return "没有找到对应玩家";
	}

	private List<GuildUserData> sort(List<GuildUserData> list) {
		Collections.sort(list, new Comparator<GuildUserData>() {

			@Override
			public int compare(GuildUserData data1, GuildUserData data2) {
				return data1.getUser().getLevel() - data1.getUser().getLevel();
			}

		});
		return list;
	}
}
