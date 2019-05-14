package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.cache.UserCache;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.GuildProp;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.model.RichGuildInfoClient;
import com.vikings.sanguo.ui.adapter.GuildUserAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomBaseListWindow;

public class GuildUserListWindow extends CustomBaseListWindow {
	private RichGuildInfoClient rgic;
	private List<Integer> members;

	@Override
	public void init() {
		adapter = new GuildUserAdapter(rgic);
		super.init("族员列表");
		setContentBelowTitle(R.layout.list_title);
		members = rgic.getMembersSequence();
	}

	public void open(RichGuildInfoClient rgic) {
		if (Account.guildCache.getGuildid() != rgic.getGuildid()) {
			controller.alert("你不是该家族成员,不能查看成员列表");
			return;
		}
		this.rgic = rgic;
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
		setTitle();
		super.showUI();
	}

	@SuppressWarnings("unchecked")
	private void checkData() {
		if (null == adapter || null == resultPage)
			return;
		List<BriefUserInfoClient> list = new ArrayList<BriefUserInfoClient>();
		list.addAll(adapter.getContent());
		List<BriefUserInfoClient> elders = new ArrayList<BriefUserInfoClient>();
		BriefUserInfoClient leader = null;
		for (int i = 0; i < list.size(); i++) {
			BriefUserInfoClient briefUser = list.get(i);
			if (!rgic.getMembers().contains(briefUser.getId())) {
				adapter.removeItem(briefUser);
				resultPage.setCurIndex(resultPage.getCurIndex() - 1);
			}
			if (rgic.isLeader(briefUser.getId().intValue())) {
				adapter.removeItem(briefUser);
				leader = briefUser;
			} else if (rgic.isElder(briefUser.getId().intValue())) {
				adapter.removeItem(briefUser);
				elders.add(briefUser);
			}
		}
		if (!elders.isEmpty()) {
			adapter.insertItemsAtHead(elders);
		}
		if (null != leader)
			adapter.insertItemAtHead(leader);
		resultPage.setCurIndex(adapter.getContent().size());
		resultPage.setTotal(rgic.getMembers().size());
	}

	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		int start = resultPage.getCurIndex();
		int end = start + resultPage.getPageSize();
		if (end > members.size())
			end = members.size();
		if (start > end) {
			resultPage.setResult(new ArrayList<Integer>());
			resultPage.setTotal(members.size());
			return;
		}

		// 取User
		List<Integer> userids = members.subList(start, end);
		List<BriefUserInfoClient> userTemps = CacheMgr.getUser(userids);
		resultPage.setResult(UserCache.sequenceByIds(userids, userTemps));
		resultPage.setTotal(members.size());
	}

	public void setTitle() {
		// GuildProp guildProp = CacheMgr.guildPropCache.getGuildProp(rgic
		// .getMembers().size());
		GuildProp guildProp = CacheMgr.guildPropCache.search(rgic.getGic()
				.getLevel());
		if (null != guildProp)
			ViewUtil.setText(
					window,
					R.id.listTitle,
					"现有" + rgic.getMembers().size() + "名成员 （最多"
							+ guildProp.getMaxMemberCnt() + "名）");
	}

	@Override
	public void handleItem(Object o) {

	}
}
