package com.vikings.sanguo.ui.alert;

import java.util.List;

import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.GuildUserData;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.model.UserStatDataClient;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.adapter.SearchFiefUserAdapter;
import com.vikings.sanguo.widget.PageListView;

public class FiefUserListTip extends PageListView {
	public static final int TYPE_FRIEND_LIST = 1;
	public static final int TYPE_BLACK_LIST = 2;

	private int type;

	public FiefUserListTip(int type) {
		super();
		this.type = type;
		setTitle();
		firstPage();
	}

	public void setTitle() {
		switch (type) {
		case TYPE_FRIEND_LIST:
			setTitle("好友列表");
			setContentTitle("请选择一名好友");
			break;
		case TYPE_BLACK_LIST:
			setTitle("仇人录");
			setContentTitle("请选择一名仇人");
			break;
		default:
			break;
		}
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return new SearchFiefUserAdapter(new CallBack() {
			@Override
			public void onCall() {
				dismiss();
			}
		});
	}

	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		if (type == TYPE_FRIEND_LIST) {
			if (null == Account.getFriendRank()) {
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
		} else if (type == TYPE_BLACK_LIST) {
			Account.getBlackList(resultPage);
		}
	}

	@Override
	protected void updateUI() {
		super.updateUI();
		dealwithEmptyAdpter();
	}

	@Override
	public void handleItem(Object o) {

	}

	@Override
	protected String getEmptyShowText() {
		if (type == TYPE_FRIEND_LIST)
			return "你当前没有好友<br/><br/>去认识几个朋友吧！";
		else
			return "你当前没有仇人";
	}
}
