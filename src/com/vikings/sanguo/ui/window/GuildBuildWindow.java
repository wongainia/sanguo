package com.vikings.sanguo.ui.window;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.message.StaticUserDataQueryResp;
import com.vikings.sanguo.model.LogInfoClient;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.protos.StaticUserDataType;
import com.vikings.sanguo.ui.adapter.GuildBuildAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.alert.GuildBuildTip;
import com.vikings.sanguo.widget.CustomBaseListWindow;

public class GuildBuildWindow extends CustomBaseListWindow {
	private long id = 0;

	@Override
	protected void init() {
		adapter = new GuildBuildAdapter();
		super.init("家族信息");
		setLeftBtn("创建家族", new OnClickListener() {
			@Override
			public void onClick(View v) {
				new GuildBuildTip().show();
			}
		});
		setContentBelowTitle(R.layout.guild_build);
		setBottomButton("查找家族", new OnClickListener() {

			@Override
			public void onClick(View v) {
				new GuildListWindow().open();
			}
		});
		setBottomPadding();
	}

	@Override
	protected byte getLeftBtnBgType() {
		return WINDOW_BTN_BG_TYPE_CLICK;
	}

	public void open() {
		doOpen();
		firstPage();
	}

	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		StaticUserDataQueryResp resp = GameBiz.getInstance()
				.staticUserDataQuery(
						StaticUserDataType.STATIC_USER_DATA_TYPE_GUILD, id,
						resultPage.getPageSize());
		List<LogInfoClient> lics = resp.getLogInfos();
		if (!lics.isEmpty()) {
			id = lics.get(lics.size() - 1).getLogInfo().getId();
			CacheMgr.fillLogUser(lics);
			CacheMgr.fillLogGuild(lics);
		}
		resultPage.setResult(lics);
		if (lics.size() < resultPage.getPageSize()) {
			resultPage.setTotal(adapter.getCount() + lics.size());
		} else {
			resultPage.setTotal(Integer.MAX_VALUE);
		}
	}

	@Override
	public void handleItem(Object o) {
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return adapter;
	}
}
