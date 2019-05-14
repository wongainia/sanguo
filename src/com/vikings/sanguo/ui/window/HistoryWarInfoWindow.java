package com.vikings.sanguo.ui.window;

import java.util.Iterator;
import java.util.List;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.message.BriefBattleLogQueryByFiefIdResp;
import com.vikings.sanguo.model.BattleLogClient;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.adapter.BattleLogAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomBaseListWindow;

/**
 * 历史战况
 */
public class HistoryWarInfoWindow extends CustomBaseListWindow implements
		CallBack {

	private BriefFiefInfoClient bfic;

	@Override
	protected void init() {
		adapter = new BattleLogAdapter();
		super.init("历史战况");
		listView.setDividerHeight(2);
		setContentBelowTitle(R.layout.list_title);
		ViewUtil.setText(window, R.id.listTitle, "战斗日志只保存7天");
	}

	public void open(BriefFiefInfoClient _briefFiefInfoClient) {
		this.bfic = _briefFiefInfoClient;
		doOpen();
		this.firstPage();
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return adapter;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void updateUI() {

		List<BattleLogClient> ls = resultPage.getResult();
		if (ls.size() > 0) {
			adapter.addItems(ls);
			resultPage.addIndex(ls.size());
		}
		adapter.notifyDataSetChanged();
		dealwithEmptyAdpter();
	}

	@Override
	protected String getEmptyShowText() {
		if (bfic.isCastle())
			return "该主城未发生过战争";
		else
			return "该领地未发生过战争";
	}

	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		BriefBattleLogQueryByFiefIdResp resp = GameBiz.getInstance()
				.briefBattleLogQueryByFiefId(bfic.getId(),
						resultPage.getCurIndex(), resultPage.getPageSize());
		List<BattleLogClient> list = BattleLogClient.convertList(
				resp.getInfos(), false);

		// 应付斌要求，如果fief是主城 历史战况只显示和自己相关的战斗,简单处理，可能删除
		if (bfic.isCastle() && null != bfic.getLord()) {
			Iterator<BattleLogClient> it = list.iterator();
			while (it.hasNext()) {
				if (it.next().getDefendUserId() != bfic.getLord().getId())
					it.remove();
			}
		}
		// //////////////////////////////////////////////////////////////////////

		resultPage.setResult(list);
		if (list.size() < resultPage.getPageSize()) {
			resultPage.setTotal(adapter.getCount() + list.size());
		} else {
			resultPage.setTotal(Integer.MAX_VALUE);
		}
	}

	@Override
	public void onCall() {
		this.adapter.notifyDataSetChanged();
	}

	@Override
	protected void destory() {
		if (adapter != null)
			super.destory();
		controller.removeContentFullScreen(window);
	}

	@Override
	public void handleItem(Object o) {

	}
}
