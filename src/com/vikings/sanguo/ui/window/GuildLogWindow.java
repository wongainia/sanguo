package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.message.StaticGuildDataQueryResp;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.GuildLogInfoClient;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.protos.StaticGuildDataType;
import com.vikings.sanguo.ui.adapter.GuildLogAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.widget.CustomBaseListWindow;

public class GuildLogWindow extends CustomBaseListWindow {

	private List<GuildLogInfoClient> guildLogList = new ArrayList<GuildLogInfoClient>();
	private int guildid;

	@Override
	protected void init() {
		adapter = new GuildLogAdapter();
		super.init("家族日志");
	}

	public void open(int guildid) {
		this.guildid = guildid;
		this.doOpen();
		firstPage();
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return adapter;
	}

	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		long id = getLastLogId(guildLogList);
		List<GuildLogInfoClient> lics = askServer(resultPage, id,
				StaticGuildDataType.STATIC_GUILD_DATA_TYPE_GUILD_LOG);
		if (lics != null) {
			guildLogList.addAll(lics);
		}
		if (lics.size() < resultPage.getPageSize()) {
			resultPage.setTotal(guildLogList.size());
		} else {
			resultPage.setTotal(Integer.MAX_VALUE);
		}
		resultPage.setResult(lics);
	}

	private long getLastLogId(List<GuildLogInfoClient> list) {
		if (null == list || list.isEmpty())
			return 0L;
		else
			return list.get(list.size() - 1).getId();
	}

	private List<GuildLogInfoClient> askServer(ResultPage resultPage, long id,
			StaticGuildDataType type) throws GameException {
		StaticGuildDataQueryResp resp = GameBiz.getInstance()
				.staticGuildDataQuery(type, guildid, id,
						resultPage.getPageSize());
		fillUsers(resp.getLogs());
		return resp.getLogs();
	}

	@SuppressWarnings("unchecked")
	private void fillUsers(List<GuildLogInfoClient> list) throws GameException {
		List<Integer> userids = new ArrayList<Integer>();
		for (GuildLogInfoClient info : list) {
			if (!userids.contains(info.getUserid()))
				userids.add(info.getUserid());
			if (!userids.contains(info.getTargetId()))
				userids.add(info.getTargetId());
		}
		
		List<BriefUserInfoClient> users = CacheMgr.userCache.get(userids);
		for (GuildLogInfoClient info : list) {
			info.setUser(CacheMgr.getUserById(info.getUserid(), users));
			info.setTarget(CacheMgr.getUserById(info.getTargetId(), users));
		}
	}

	@Override
	public void handleItem(Object o) {

	}
}
