package com.vikings.sanguo.ui.window;

import java.util.List;

import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.message.StaticUserDataQueryResp;
import com.vikings.sanguo.model.BattleLogClient;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.protos.StaticUserDataType;
import com.vikings.sanguo.ui.adapter.BattleLogAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.widget.CustomBaseListWindow;

public class LogDefendWindow extends CustomBaseListWindow {

	private ObjectAdapter adapter = new BattleLogAdapter();

	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		BattleLogClient log = (BattleLogClient) adapter.getLast();
		StaticUserDataQueryResp resp = GameBiz
				.getInstance()
				.staticUserDataQuery(
						StaticUserDataType.STATIC_USER_DATA_TYPE_BFIEF_BATTLE_LOG_DEFEND,
						log == null ? 0 : log.getId(), resultPage.getPageSize());
		List<BattleLogClient> lics = BattleLogClient.convertList(resp
				.getBriefBattleLogs(),true);
		if (lics.size() < resultPage.getPageSize()) {
			resultPage.setTotal(adapter.getCount() + lics.size());
		} else {
			resultPage.setTotal(Integer.MAX_VALUE);
		}
		resultPage.setResult(lics);
	}

	@Override
	public void handleItem(Object o) {
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return adapter;
	}

	@Override
	protected void init() {
		init("遇袭日志");
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
		return "暂无遇袭日志";
	}
}
