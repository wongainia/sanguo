package com.vikings.sanguo.ui.window;

import android.view.View;
import android.view.View.OnClickListener;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.thread.ViewImgCallBack;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomListViewWindow;

public class OptionsSearchWindow extends CustomListViewWindow {

	private String[] imgResNames = { "title_friend_01.png",
			"title_friend_02.png" };
	private String[] titles = { "同国玩家", "按条件查找" };
	private String[] descs = { "—查找和你同国的玩家", "—通过指定条件查找" };

	private OnClickListener[] listeners = { new OnClickListener() {

		@Override
		public void onClick(View v) {
			new SearchCountryUserResultWindow(Account.user.getCountry()
					.intValue()).open();

		}
	}, new OnClickListener() {

		@Override
		public void onClick(View v) {
			controller.openSearchWindow();

		}

	} };

	public void open() {
		doOpen();
	}

	@Override
	protected void init() {
		adapter = new OptionsSearchAdapter();
		adapter.addItems(titles);
		super.init("添加好友");
	}

	private class OptionsSearchAdapter extends ObjectAdapter {

		@Override
		public void setViewDisplay(View v, Object o, int index) {
			new ViewImgCallBack(imgResNames[index], v.findViewById(R.id.img));
			ViewUtil.setText(v, R.id.title, titles[index]);
			ViewUtil.setText(v, R.id.desc, descs[index]);
			v.setOnClickListener(listeners[index]);
		}

		@Override
		public int getLayoutId() {
			return R.layout.options_search_item;
		}

	}

	@Override
	protected ObjectAdapter getAdapter() {
		return adapter;
	}
}
