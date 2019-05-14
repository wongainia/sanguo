package com.vikings.sanguo.widget;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;

public interface ArenaWindowTab {
	public void getServerData(ResultPage resultPage) throws GameException;
	public void showUI();
	public ObjectAdapter getAdapter();
	public int refreshInterval();
	public boolean needScroll();
	public String getEmptyShowText();
}
