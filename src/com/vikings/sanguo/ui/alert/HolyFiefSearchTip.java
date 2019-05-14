package com.vikings.sanguo.ui.alert;

import java.util.List;

import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.message.QueryHolyBattleStateResp;
import com.vikings.sanguo.model.HolyBattleStateClient;
import com.vikings.sanguo.model.HolyCategory;
import com.vikings.sanguo.model.HolyProp;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.adapter.SearchHolyFiefAdapter;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.widget.PageListView;

public class HolyFiefSearchTip extends PageListView {
	private HolyCategory hc;

	public HolyFiefSearchTip(HolyCategory hc) {
		super();
		this.hc = hc;
		if (null != adapter)
			((SearchHolyFiefAdapter) adapter).setType(hc.getCategory());
		setTitle(hc.getBtnTxt());
		setContentTitle(hc.getBtnTxt() + "信息");
		firstPage();
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return new SearchHolyFiefAdapter();
	}

	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		List<HolyProp> hpHolyProps = null;
		if (hc.getCategory() != HolyCategory.SHENJI) {
			QueryHolyBattleStateResp resp = GameBiz.getInstance()
					.queryHolyBattleState(Account.user.getCountry());

			List<HolyBattleStateClient> hbscBattleStateClients = HolyBattleStateClient
					.getHbscsByType(hc.getCategory(), resp.getList());

			// 同步服务器的状态
			hpHolyProps = HolyBattleStateClient.getBattleHolyProp(
					hbscBattleStateClients, hc);
		} else {
			// 不需要查询战斗状态 减少访问数据量
			hpHolyProps = CacheMgr.holyPropCache.getHolyPropsByCategory(hc
					.getCategory());
		}

		// 可以少查询一些数据BriefFiefInfoClient
		CacheMgr.fillHolyBattleState(hpHolyProps);

		if (!ListUtil.isNull(hpHolyProps)) {
			resultPage.setResult(hpHolyProps);
			resultPage.setTotal(hpHolyProps.size());
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
		Config.getController().getBattleMap()
				.moveToFief(((HolyProp) o).getFiefId(), true, true);
	}

	@Override
	protected String getEmptyShowText() {
		return "";
	}
}
