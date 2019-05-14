package com.vikings.sanguo.ui.window;

import java.util.List;
import java.util.ArrayList;
import android.view.View;
import android.view.View.OnClickListener;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.GuildUserData;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.ui.adapter.BlackListAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.alert.AddBlackListTip;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomBaseListWindow;

public class BlackListWindow extends CustomBaseListWindow {

	@Override
	protected void init() {
		adapter = new BlackListAdapter();
		super.init("仇人录");
		setLeftBtn("好友列表", new OnClickListener() {

			@Override
			public void onClick(View v) {
				controller.goBack();
			}
		});
		setContentBelowTitle(R.layout.list_title);
		setBottomButton("添加仇人", new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AddBlackListTip().show();
			}
		});
		setBottomPadding();
	}
	
	//框体左按钮点击事件
	@Override
	protected byte getLeftBtnBgType() {
		return WINDOW_BTN_BG_TYPE_CLICK;
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
		setTitle(Account.blackList.size());
		super.showUI();
	}

	@SuppressWarnings("unchecked")
	private void checkData() {
		if (null == adapter || null == resultPage)
			return;
		List<GuildUserData> list = new ArrayList<GuildUserData>();
		list.addAll(adapter.getContent());
		for (GuildUserData data : list) {
			if (!Account.blackList.contains(data.getUser().getId().intValue())) {
				adapter.removeItem(data);
				resultPage.setCurIndex(resultPage.getCurIndex() - 1);
			}
		}
		resultPage.setCurIndex(adapter.getContent().size());
		resultPage.setTotal(Account.blackList.size());
	}

	@Override
	public void handleItem(Object o) {
	}

	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		Account.getBlackList(resultPage);
	}

	public void setTitle(int total) {
		int toplimit = CacheMgr.dictCache.getDictInt(Dict.TYPE_BATTLE_COST, 9);
		ViewUtil.setText(window, R.id.listTitle,
				"仇人录中有" + Account.blackList.size() + "人 （最多" + toplimit + "人）");
	}
}
