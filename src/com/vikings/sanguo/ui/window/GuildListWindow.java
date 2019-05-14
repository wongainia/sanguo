package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.GuildSearchInfoClient;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.protos.GuildSearchCond;
import com.vikings.sanguo.protos.GuildSearchFieldType;
import com.vikings.sanguo.ui.adapter.GuildAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomBaseListWindow;

public class GuildListWindow extends CustomBaseListWindow {
	private List<GuildSearchCond> conds;

	@Override
	protected void init() {
		adapter = new GuildAdapter();
		super.init("已有家族");
		setContentBelowTitle(R.layout.list_title);
		setTitle();
		conds = new ArrayList<GuildSearchCond>();
		GuildSearchCond cond = new GuildSearchCond();
		cond.setField(GuildSearchFieldType.GUILD_SEARCH_GUILD.getNumber());
		cond.setIntValue(Account.user.getCountry());
		conds.add(cond);
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
	public void handleItem(Object o) {
	}

	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		List<GuildSearchInfoClient> datas = GameBiz.getInstance().guildSearch(
				resultPage, conds);
		resultPage.setResult(datas);
		if (datas.size() < resultPage.getPageSize())
			resultPage.setTotal(adapter.getCount() + datas.size());
		else
			resultPage.setTotal(Integer.MAX_VALUE);

	}

	public void setTitle() {
		ViewUtil.setText(window, R.id.listTitle, "本国已有家族列表");
	}
}
