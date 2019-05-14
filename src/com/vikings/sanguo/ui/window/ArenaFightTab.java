package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.message.ArenaQueryResp;
import com.vikings.sanguo.model.ArenaUserRankInfoClient;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.ui.adapter.ArenaUserAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.ArenaWindowTab;
import com.vikings.sanguo.widget.CustomBaseListWindow;

public class ArenaFightTab implements ArenaWindowTab {
	private List<ArenaUserRankInfoClient> topUsers = new ArrayList<ArenaUserRankInfoClient>();
	private List<ArenaUserRankInfoClient> attackUsers = new ArrayList<ArenaUserRankInfoClient>();
	private ArenaUserAdapter userAdapter;
	private int preArenaRank = -1;
	private CustomBaseListWindow window;
	private List<ArenaUserRankInfoClient> list;
	private ArenaUserRankInfoClient arenaUserRankInfoClient;

	public void setWindow(CustomBaseListWindow window) {
		this.window = window;
	}

	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		if (needRefresh()) {
			preArenaRank = Account.myLordInfo.getArenaRank();
			boolean isFirst = ListUtil.isNull(topUsers) ? true : false;
			ArenaQueryResp resp = GameBiz.getInstance().arenaQuery(isFirst);
			if (!ListUtil.isNull(resp.getTopUsers())) {
				topUsers.clear();
				topUsers.addAll(resp.getTopUsers());
			}
			if (!ListUtil.isNull(resp.getAttackableUsers())) {
				attackUsers.clear();
				attackUsers.addAll(resp.getAttackableUsers());
			}
			list = prepareUsers();
		}
		if (null != list) {
			resultPage.setResult(list);
			resultPage.setTotal(list.size());
		}
	}

	private boolean needRefresh() {
		return preArenaRank != Account.myLordInfo.getArenaRank();
	}

	private List<ArenaUserRankInfoClient> prepareUsers() {
		mergeArenaUsers();

		List<ArenaUserRankInfoClient> ls = new ArrayList<ArenaUserRankInfoClient>();
		if (!ListUtil.isNull(topUsers))
			ls.addAll(topUsers);
		if (!ListUtil.isNull(attackUsers))
			ls.addAll(attackUsers);

		// 添加自己到列表末尾
		arenaUserRankInfoClient = Account.myLordInfo
				.getArenaUserRankInfoClient();
		if (arenaUserRankInfoClient != null
				&& !ls.contains(arenaUserRankInfoClient))
			ls.add(arenaUserRankInfoClient);

		return sortArenaUsers(ls);
	}

	private void mergeArenaUsers() {
		if (!ListUtil.isNull(topUsers)) {
			Iterator<ArenaUserRankInfoClient> itTop = topUsers.iterator();
			while (itTop.hasNext()) {
				ArenaUserRankInfoClient nextTop = itTop.next();

				for (ArenaUserRankInfoClient it : attackUsers) {
					if (it.getUser().getId().intValue() == nextTop.getUser()
							.getId().intValue()) {
						itTop.remove();
						break;
					}
				}
			}
		}
	}

	private List<ArenaUserRankInfoClient> sortArenaUsers(
			List<ArenaUserRankInfoClient> ls) {
		Collections.sort(ls, new Comparator<ArenaUserRankInfoClient>() {
			@Override
			public int compare(ArenaUserRankInfoClient object1,
					ArenaUserRankInfoClient object2) {
				if (object1.hasRank() && !object2.hasRank()) // 有排名的在前
					return -1;
				else if (!object1.hasRank() && object2.hasRank())
					return 1;
				else if (!object1.hasRank() && !object2.hasRank()) { // 都没有排名，自己排在最后
					if (object1.getUserId() == Account.user.getId()
							&& object2.getUserId() != Account.user.getId())
						return 1;
					else if (object1.getUserId() != Account.user.getId()
							&& object2.getUserId() == Account.user.getId())
						return -1;
					else
						return 0;
				} else
					return object1.getRank() - object2.getRank();
			}
		});
		return ls;
	}

	@Override
	public void showUI() {
		if (ViewUtil.isVisible(window.findViewById(R.id.bonusLayout)))
			ViewUtil.setGone(window.findViewById(R.id.bonusLayout));

		if (ViewUtil.isGone(window.findViewById(R.id.gradientMsgLayout)))
			ViewUtil.setVisible(window.findViewById(R.id.gradientMsgLayout));
		ViewUtil.setText(window.findViewById(R.id.gradientMsg),
				"击败高名次的对手，取代他的名次赢取更多功勋");

		if (needRefresh()) {
			window.firstPage();
			if (!ListUtil.isNull(topUsers)
			// 非定级赛和排名上升不显示
					&& (preArenaRank > 0 && preArenaRank < Account.myLordInfo
							.getArenaRank()))
				Config.getController().alert("您的战场排名已发生变化，正在获取最新数据！");
		} else {
			if (Account.myLordInfo.isArenaConfig() && null != userAdapter
					&& null != arenaUserRankInfoClient) {
				arenaUserRankInfoClient.update(Account.myLordInfo
						.getArenaUserRankInfoClient());
				userAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public ObjectAdapter getAdapter() {
		if (null == userAdapter)
			userAdapter = new ArenaUserAdapter(topUsers, attackUsers);
		return userAdapter;
	}

	@Override
	public int refreshInterval() {
		return 10 * 1000;
	}

	@Override
	public boolean needScroll() {
		return false;
	}

	@Override
	public String getEmptyShowText() {
		return "暂时没有挑战对象";
	}
}
