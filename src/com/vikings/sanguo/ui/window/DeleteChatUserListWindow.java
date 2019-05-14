package com.vikings.sanguo.ui.window;

import java.util.List;
import java.util.ArrayList;
import android.view.View;
import android.view.View.OnClickListener;
import com.vikings.sanguo.Constants;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.cache.UserCache;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.ui.adapter.DeleteChatUserAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.widget.CustomBaseListWindow;

public class DeleteChatUserListWindow extends CustomBaseListWindow {

	private List<Integer> userIds;

	@Override
	protected void init() {
		adapter = new DeleteChatUserAdapter(userIds);
		super.init("删除信息");
		setLeftBtn("完成", new OnClickListener() {
			@Override
			public void onClick(View v) {
				controller.goBack();
			}
		});
	}
	
	

	@Override
	protected byte getLeftBtnBgType() {
		return WINDOW_BTN_BG_TYPE_CLICK;
	}



	public void open(List<Integer> userIds) {
		this.userIds = userIds;
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
		if (temp.contains(Integer.valueOf(Constants.GUILD_ID)))
			temp.remove(Integer.valueOf(Constants.GUILD_ID));
		if (temp.contains(Integer.valueOf(Constants.WORLD_ID)))
			temp.remove(Integer.valueOf(Constants.WORLD_ID));
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

}
