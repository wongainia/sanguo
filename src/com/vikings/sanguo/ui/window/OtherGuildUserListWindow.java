package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.cache.UserCache;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.GuildProp;
import com.vikings.sanguo.model.OtherRichGuildInfoClient;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.adapter.OtherGuildUserAdapter;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomBaseListWindow;

public class OtherGuildUserListWindow extends CustomBaseListWindow {
	private OtherRichGuildInfoClient orgic;
	private List<Integer> members;

	@Override
	public void init() {
		adapter = new OtherGuildUserAdapter(orgic);
		super.init("现有成员");
		setContentBelowTitle(R.layout.list_title);
		members = orgic.getMembersSequence();
	}

	public void open(OtherRichGuildInfoClient orgic) {
		this.orgic = orgic;
		doOpen();
		firstPage();
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return adapter;
	}

	@Override
	public void showUI() {
		setTitle();
		super.showUI();
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
		// GuildProp guildProp = CacheMgr.guildPropCache.getGuildProp(orgic
		// .getMembers().size());
		GuildProp guildProp = CacheMgr.guildPropCache.search(orgic.getOgic()
				.getLevel());
		if (null != guildProp)
			ViewUtil.setText(
					window,
					R.id.listTitle,
					"现有" + orgic.getMembers().size() + "名成员 （最多"
							+ guildProp.getMaxMemberCnt() + "名）");
	}

	@Override
	public void handleItem(Object o) {

	}
}
