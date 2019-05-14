/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-12-10 上午10:25:00
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.window;

import java.util.List;
import android.view.ViewGroup;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.message.StaticUserDataQueryResp;
import com.vikings.sanguo.model.ArenaLogInfoClient;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.protos.StaticUserDataType;
import com.vikings.sanguo.ui.adapter.ArenaLogAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.ArenaWindowTab;

public class ArenaLogTab implements ArenaWindowTab {
	private ArenaLogAdapter logAdapter;
	private ViewGroup window;

	public void setWindow(ViewGroup window) {
		this.window = window;
	}

	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		ArenaLogInfoClient log = (ArenaLogInfoClient) logAdapter.getLast();
		StaticUserDataQueryResp resp = GameBiz
				.getInstance()
				.staticUserDataQuery(
						StaticUserDataType.STATIC_USER_DATA_TYPE_ARENA_LOG,
						log == null ? 0 : log.getId(), resultPage.getPageSize());
		List<ArenaLogInfoClient> logs = ArenaLogInfoClient.convert2List(resp
				.getArenaLogs());
		if (logs.size() < resultPage.getPageSize())
			resultPage.setTotal(logAdapter.getCount() + logs.size());
		else
			resultPage.setTotal(Integer.MAX_VALUE);
		resultPage.setResult(logs);
	}

	@Override
	public void showUI() {
		if (ViewUtil.isVisible(window.findViewById(R.id.bonusLayout)))
			ViewUtil.setGone(window, R.id.bonusLayout);

		if (ViewUtil.isVisible(window.findViewById(R.id.gradientMsgLayout)))
			ViewUtil.setGone(window.findViewById(R.id.gradientMsgLayout));
	}

	@Override
	public ObjectAdapter getAdapter() {
		if (null == logAdapter)
			logAdapter = new ArenaLogAdapter();
		return logAdapter;
	}

	@Override
	public int refreshInterval() {
		return 0;
	}

	@Override
	public boolean needScroll() {
		return true;
	}

	@Override
	public String getEmptyShowText() {
		return "暂时没有巅峰战场日志";
	}
}
