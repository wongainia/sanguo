package com.vikings.sanguo.ui.alert;

import java.util.List;

import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.HolyBattleStateClient;
import com.vikings.sanguo.model.HolyCategory;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.adapter.SearchHolyAdapter;
import com.vikings.sanguo.ui.guide.UIChecker;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.widget.PageListView;
import com.vikings.sanguo.message.*;

public class ForeignInvasionFiefTip extends PageListView {

	public ForeignInvasionFiefTip() {
		super();
		setContentTitleGone();
		refreshInterval = 1000;
		setTitle("外敌入侵");
		firstPage();
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return new SearchHolyAdapter();
	}

	@Override
	protected void updateDynView() {
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		QueryHolyBattleStateResp resp = GameBiz.getInstance()
				.queryHolyBattleState(Account.user.getCountry());
		List<HolyCategory> ls = CacheMgr.holyCategoryCache.getAll();
		ls = HolyBattleStateClient.fillHolycByhbsc(ls, resp.getList());
		if (!ListUtil.isNull(ls)) {
			resultPage.setResult(ls);
			resultPage.setTotal(ls.size());
		}

	}

	@Override
	protected void updateUI() {
		super.updateUI();
		dealwithEmptyAdpter();
	}

	@Override
	public void handleItem(Object o) {
		dismiss();
		if (Account.user.getLevel() < UIChecker.FUNC_BATTLE) {
			if (Account.getCurVip() == null
					|| Account.getCurVip().getLevel() <= 0) {
				new ToMapTip().show();
			} else {
				controller.alert("出征功能" + UIChecker.FUNC_BATTLE + "级开启");
			}
			return;
		}
		Config.getController().getFiefMap().open();

		Config.getController().openHolyFiefDetail((HolyCategory) o);
	}

	@Override
	protected String getEmptyShowText() {
		return "";
	}
}
