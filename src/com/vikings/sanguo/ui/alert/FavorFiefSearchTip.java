/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-5-4 下午6:58:56
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.alert;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.adapter.SearchFiefAdapter;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.widget.PageListView;

public class FavorFiefSearchTip extends PageListView {
	private List<Long> ids = new ArrayList<Long>();
	private List<BriefFiefInfoClient> fiefs = null;
	private int index = -1;
	private BriefUserInfoClient user;
	private CallBack callBack;

	public FavorFiefSearchTip(int index) {
		this(index, null);
	}

	public FavorFiefSearchTip(int index, BriefUserInfoClient user) {
		this(index, user, null);
	}

	public FavorFiefSearchTip(int index, BriefUserInfoClient user,
			CallBack callBack) {
		super();
		this.index = index;
		this.user = user;
		this.callBack = callBack;
		if (null != adapter) {
			((SearchFiefAdapter) adapter).setType(index);
		}
		setTitle();
		firstPage();
	}

	public void setTitle() {
		switch (index) {
		case Constants.FAVOR:
			setTitle("领地收藏夹");
			setContentTitle("总共收藏了  "
					+ StringUtil.color("" + Account.getFavoriteFief().size(),
							R.color.color19) + "  块领地");
			if (null != adapter) {
				((SearchFiefAdapter) adapter).setCallBack(new DeleteCallBack());
			}
			break;
		case Constants.HOLY:
			setContentTitle("圣都信息");
			setTitle("圣  都");
			break;
		case Constants.FAMOUS:
			setContentTitle("名城信息");
			setTitle("名  城");
			break;
		case Constants.USER:
			setContentTitle("领地列表");
			if (null != user)
				setTitle(user.getNickName() + " 的领地");
			// setContentTitleGone();
			break;
		default:
			break;
		}
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return new SearchFiefAdapter();
	}

	private class DeleteCallBack implements CallBack {
		@Override
		public void onCall() {
			List<Long> curIds = Account.getFavoriteFief();
			if (curIds.isEmpty()) {
				dismiss();
			} else {
				if (null != adapter && null != ids
						&& curIds.size() != ids.size()) {
					List<BriefFiefInfoClient> fiefs = new ArrayList<BriefFiefInfoClient>();
					fiefs.addAll(adapter.getContent());
					for (BriefFiefInfoClient fief : fiefs) {
						if (!curIds.contains(fief.getId())) {
							if (ids.contains(fief.getId()))
								ids.remove(fief.getId());
							adapter.removeItem(fief);
						}
					}

					setContentTitle("总共收藏了" + curIds.size() + "块领地");
				}
			}
		}
	}

	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		if (Constants.FAVOR == index) {
			if (Account.hasFavorateFief())
				ids = Account.getFavoriteFief();
		} else if (Constants.USER == index) {
			ids = GameBiz.getInstance().lordFiefIdQuery(user.getId());

		}
		setFief();
	}

	private void setFief() throws GameException {
		int start = resultPage.getCurIndex();
		int end = start + resultPage.getPageSize();
		if (end > ids.size())
			end = ids.size();
		if (start > end) {
			resultPage.setResult(new ArrayList<Integer>());
			resultPage.setTotal(ids.size());
			return;
		}
		fiefs = GameBiz.getInstance().briefFiefInfoQuery(
				ids.subList(start, end));
		resultPage.setResult(fiefs);
		resultPage.setTotal(ids.size());
	}

	@Override
	protected void updateUI() {
		super.updateUI();
		dealwithEmptyAdpter();
	}

	@Override
	public void handleItem(Object o) {
		dismiss();
		if (null != callBack)
			callBack.onCall();
		BriefFiefInfoClient bfic = (BriefFiefInfoClient) o;
		Config.getController().getBattleMap()
				.moveToFief(bfic.getId(), true, true);
	}

	@Override
	protected String getEmptyShowText() {
		if (Constants.USER == index)
			return "该玩家一块领地都没有";
		else
			return "";
	}
}
