package com.vikings.sanguo.ui.window;

import java.util.List;
import java.util.ArrayList;

import android.view.View;
import android.view.View.OnClickListener;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.cache.UserCache;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.ui.adapter.ChatUserAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.widget.CustomBaseListWindow;

public class ChatUserListWindow extends CustomBaseListWindow {

	private List<Integer> userIds;

	@Override
	protected void bindField() {
		adapter = new ChatUserAdapter();
		super.init("信息列表");
	}

	@Override
	protected void init() {
		List<Integer> newUserIds = Account.msgInfoCache.getUserIds();
		if (null == userIds) {
			userIds = newUserIds;
		} else {
			userIds.clear();
			userIds.addAll(newUserIds);
		}
		setLeftBtn("删除", new OnClickListener() {
			@Override
			public void onClick(View v) {
				new DeleteChatUserListWindow().open(userIds);
			}
		});
	}

	public void open() {
		doOpen();
		firstPage();
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return adapter;
	}

	@Override
	public void showUI() {
		checkData();
		// setTitle(Account.friends.size());
		super.showUI();
	}

	@SuppressWarnings("unchecked")
	private void checkData() {
		if (null == adapter || null == resultPage)
			return;
		List<BriefUserInfoClient> list = new ArrayList<BriefUserInfoClient>();
		list.addAll(adapter.getContent());
		for (BriefUserInfoClient briefUser : list) {
			if (!userIds.contains(briefUser.getId().intValue())) {
				adapter.removeItem(briefUser);
				resultPage.setCurIndex(resultPage.getCurIndex() - 1);
			}
		}
		resultPage.setCurIndex(adapter.getContent().size());
		resultPage.setTotal(userIds.size());
	}

	@Override
	protected void destory() {
	}

	@Override
	public void handleItem(Object o) {
	}

	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		synchronized (userIds) {
			int start = adapter.getCount();
			int end = start + resultPage.getPageSize();
			if (end > userIds.size())
				end = userIds.size();
			if (start > end) {
				resultPage.setResult(new ArrayList<Integer>());
				resultPage.setTotal(userIds.size());
				return;
			}

			// 取User
			List<Integer> subIds = userIds.subList(start, end);
			resultPage.setResult(UserCache.sequenceByIds(subIds,
					getUsers(subIds)));
			resultPage.setTotal(userIds.size());
		}
	}

	private List<BriefUserInfoClient> getUsers(List<Integer> ids)
			throws GameException {
		List<Integer> temp = new ArrayList<Integer>();
		temp.addAll(ids);
		if (temp.contains(Integer.valueOf(Constants.COUNTRY_ID)))
			temp.remove(Integer.valueOf(Constants.COUNTRY_ID));
		if (temp.contains(Integer.valueOf(Constants.WORLD_ID)))
			temp.remove(Integer.valueOf(Constants.WORLD_ID));
		if (temp.contains(Integer.valueOf(Constants.GUILD_ID)))
			temp.remove(Integer.valueOf(Constants.GUILD_ID));
		List<BriefUserInfoClient> users = CacheMgr.getUser(temp);
		if (ids.contains(Integer.valueOf(Constants.COUNTRY_ID))) {
			BriefUserInfoClient fakeBrief = new BriefUserInfoClient();
			fakeBrief.setId(Constants.COUNTRY_ID);
			users.add(fakeBrief);
		}

		if (ids.contains(Integer.valueOf(Constants.GUILD_ID))) {
			BriefUserInfoClient fakeBrief = new BriefUserInfoClient();
			fakeBrief.setId(Constants.GUILD_ID);
			users.add(fakeBrief);
		}

		if (ids.contains(Integer.valueOf(Constants.WORLD_ID))) {
			BriefUserInfoClient fakeBrief = new BriefUserInfoClient();
			fakeBrief.setId(Constants.WORLD_ID);
			users.add(fakeBrief);
		}

		return users;
	}

	// public void setTitle(int total) {
	// int toplimit = CacheMgr.dictCache.getDictInt(Dict.TYPE_BATTLE_COST, 22);
	// ViewUtil.setText(window, R.id.listTitle, "拥有" + Account.friends.size()
	// + "名好友 （最多" + toplimit + "名）");
	// }

	public void addChatUsers(final List<BriefUserInfoClient> briefUsers) {
		if (null == userIds)
			return;
		for (int i = briefUsers.size() - 1; i >= 0; i--) {
			BriefUserInfoClient briefUser = briefUsers.get(i);
			if (userIds.contains(briefUser.getId().intValue())) {
				userIds.remove(briefUser.getId());
				userIds.add(0, briefUser.getId().intValue());
			} else {
				userIds.add(0, briefUser.getId().intValue());
			}
		}
		content.post(new Runnable() {
			@Override
			public void run() {
				adapter.insertItemsAtHead(briefUsers);
				adapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	protected byte getLeftBtnBgType() {
		return WINDOW_BTN_BG_TYPE_CLICK;
	}
}
