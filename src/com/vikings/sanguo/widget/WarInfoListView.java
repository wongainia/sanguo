/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-8-27 下午5:08:03
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.widget;

import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BattleHeroInfoClient;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.FiefProp;
import com.vikings.sanguo.model.MoveTroopInfoClient;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.protos.UserTroopEffectInfos;
import com.vikings.sanguo.ui.adapter.WarInfosAdapter;
import com.vikings.sanguo.utils.ListUtil;

public class WarInfoListView extends CustomListView {
	private UserTroopEffectInfos userTroopEffectInfos;
	private List<MoveTroopInfoClient> moveTroopInfos;
	private FiefProp fiefProp;
	private List<BattleHeroInfoClient> heroInfos;

	public WarInfoListView(ExpandableListView listView, View loadView,
			UserTroopEffectInfos userTroopEffectInfos, FiefProp fiefProp,
			List<MoveTroopInfoClient> moveTroopInfos,
			List<BattleHeroInfoClient> heroInfos) {
		super(listView, loadView, null);
		this.userTroopEffectInfos = userTroopEffectInfos;
		this.moveTroopInfos = moveTroopInfos;
		this.fiefProp = fiefProp;
		this.heroInfos = heroInfos;

		setAdapterToListView();

		expandListView();
		firstPage();
	}

	private void expandListView() {
		for (int i = 0; i < this.moveTroopInfos.size(); i++)
			this.listView.expandGroup(i);

		this.listView.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				return true;
			}
		});
	}

	@Override
	protected BaseExpandableListAdapter getAdapter() {
		return new WarInfosAdapter(listView, userTroopEffectInfos, fiefProp);
	}

	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		int start = resultPage.getCurIndex();
		int end = start + resultPage.getPageSize();
		if (end > moveTroopInfos.size())
			end = moveTroopInfos.size();
		if (start > end) {
			resultPage.setResult(new ArrayList<Integer>());
			resultPage.setTotal(moveTroopInfos.size());
			return;
		}

		List<MoveTroopInfoClient> tmp = moveTroopInfos.subList(start, end);
		List<Integer> ids = new ArrayList<Integer>();
		for (MoveTroopInfoClient it : tmp) {
			if (!ids.contains(it.getUserid()) && it.getUserid() >= 0)
				ids.add(it.getUserid());
		}

		List<BriefUserInfoClient> users = CacheMgr.userCache.get(ids);

		for (BriefUserInfoClient user : users) {
			for (MoveTroopInfoClient it : tmp) {
				if (it.getUserid() == user.getId())
					it.setUser(user);
			}
		}

		resultPage.setResult(tmp);
		resultPage.setTotal(moveTroopInfos.size());
	}

	@Override
	public void handleItem(Object o) {

	}

	@Override
	protected void updateUI() {
		setAdapter(moveTroopInfos, heroInfos);
		super.updateUI();
	}

	public void updateAdpater(List<MoveTroopInfoClient> moveTroopInfos,
			List<BattleHeroInfoClient> heroInfos,
			UserTroopEffectInfos userTroopEffectInfos) {
		this.userTroopEffectInfos = userTroopEffectInfos;
		this.moveTroopInfos = moveTroopInfos;
		this.heroInfos = heroInfos;
		expandListView();
		// 保证取到User信息
		firstPage();
	}

	private void setAdapter(List<MoveTroopInfoClient> moveTroopInfos,
			List<BattleHeroInfoClient> heroInfos) {
		((WarInfosAdapter) adapter).setItem(moveTroopInfos);
		if (!ListUtil.isNull(heroInfos))
			((WarInfosAdapter) adapter).setHeroInfos(heroInfos);
	}
}
