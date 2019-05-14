/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-1-16 下午4:21:59
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.window;

import java.util.List;

import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BattleHotInfoClient;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.ui.adapter.HotBattleLogAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.widget.CustomBaseListWindow;

public class HotBattleLogWindow extends CustomBaseListWindow {

	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		List<BattleHotInfoClient> info = GameBiz.getInstance().battleHotInfo();
		resultPage.setResult(info);
		resultPage.setTotal(info.size());
	}

	@Override
	public void handleItem(Object o) {
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return new HotBattleLogAdapter();
	}

	@Override
	protected void init() {
		init("大战役日志");
	}

	public void open() {
		this.doOpen();
		this.firstPage();
	}

	@Override
	protected void updateUI() {
		super.updateUI();
		dealwithEmptyAdpter();
	}

	@Override
	protected String getEmptyShowText() {
		return "暂无大战役日志";
	}

}
