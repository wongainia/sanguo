package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.GuildUserData;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.model.UserStatDataClient;
import com.vikings.sanguo.ui.adapter.FriendAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomBaseListWindow;

public class FriendListWindow extends CustomBaseListWindow {
	@Override
	protected void init() {
		adapter = new FriendAdapter();
		super.init("好友列表");
		setLeftBtn("仇人录", new OnClickListener() {
			@Override
			public void onClick(View v) {
				new BlackListWindow().open();
			}
		});
		setContentBelowTitle(R.layout.list_title);// 列表上端横条描述
		setBottomButton("添加好友", new OnClickListener() {

			@Override
			public void onClick(View v) {
				controller.showOptionsWindow();
			}
		});
		setBottomPadding();
	}

	public void open() {
		doOpen();
		firstPage();
	}

	// 框体左按钮背景
	@Override
	protected byte getLeftBtnBgType() {
		return WINDOW_BTN_BG_TYPE_CLICK;
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return adapter;
	}

	@Override
	public void showUI() {
		checkData();
		setTitle(Account.friends.size());
		super.showUI();
	}

	@Override
	protected void updateUI() {
		if (Account.friends.size() <= 0) {
			ViewUtil.setGone(listView);
			ViewUtil.setVisible(emptyShow);
			ViewUtil.setRichText(window, R.id.emptyDesc, getEmptyShowText());
		} else {
			ViewUtil.setVisible(listView);
			ViewUtil.setGone(emptyShow);
		}
		super.updateUI();
	}

	@SuppressWarnings("unchecked")
	private void checkData() {
		if (null == adapter || null == resultPage)
			return;
		List<GuildUserData> list = new ArrayList<GuildUserData>();
		list.addAll(adapter.getContent());
		for (GuildUserData data : list) {
			if (!Account.friends.contains(data.getUser().getId().intValue())) {
				adapter.removeItem(data);
				resultPage.setCurIndex(resultPage.getCurIndex() - 1);
			}
		}
		resultPage.setCurIndex(adapter.getContent().size());
		resultPage.setTotal(Account.friends.size());
		// 解决没有内容时,添加好友后列表不显示
		if (adapter.getContent().size() <= 0 && Account.friends.size() > 0)
			listView.invalidate();

		if (Account.friends.size() <= 0) {
			ViewUtil.setGone(listView);
			ViewUtil.setVisible(emptyShow);
			ViewUtil.setRichText(window, R.id.emptyDesc, getEmptyShowText());
		} else {
			ViewUtil.setVisible(listView);
			ViewUtil.setGone(emptyShow);
		}
	}

	@Override
	protected String getEmptyShowText() {
		return "你当前没有好友<br/><br/>去认识几个朋友吧！";
	}

	@Override
	public void handleItem(Object o) {
	}

	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		if (null == Account.getFriendRank()) {// 好友排名
			List<UserStatDataClient> datas = GameBiz.getInstance()
					.queryFriendRank(Account.friends);
			Account.setFriendRank(datas);
		}
		Account.sortFriend();
		List<GuildUserData> datas = Account.getFriend(resultPage);
		for (GuildUserData data : datas) {
			if (null != data.getUser())
				Account.updateFriendRank(data.getUser());
		}
	}

	public void setTitle(int total) {
		int toplimit = CacheMgr.dictCache.getDictInt(Dict.TYPE_BATTLE_COST, 22);
		ViewUtil.setText(window, R.id.listTitle, "拥有" + Account.friends.size()
				+ "名好友 （最多" + toplimit + "名）");
	}
}
