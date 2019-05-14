package com.vikings.sanguo.ui.window;

import android.view.View;
import android.view.View.OnClickListener;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.model.UITextProp;
import com.vikings.sanguo.thread.ViewImgCallBack;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomListViewWindow;

public class LogWindow extends CustomListViewWindow {

	@Override
	protected ObjectAdapter getAdapter() {
		LogAdapter adapter = new LogAdapter();
		adapter.addItems(text);
		return adapter;
	}

	@Override
	protected void init() {
		init("战争日志");
	}

	private class LogAdapter extends ObjectAdapter {

		@Override
		public void setViewDisplay(View v, Object o, int index) {
			new ViewImgCallBack(icon[index], v.findViewById(R.id.logIcon));
			ViewUtil.setBoldText(v.findViewById(R.id.name), text[index]);
			ViewUtil.setRichText(v.findViewById(R.id.desc), ViewUtil
					.half2Full(CacheMgr.uiTextCache.getTxt(descs[index]
							.intValue())));

			v.setOnClickListener(onclick[index]);
		}

		@Override
		public int getLayoutId() {
			return R.layout.log_item;
		}

	}

	private String[] icon = { "title_record_01.png", "title_record_02.png",
			"title_record_03.png", "title_record_04.png" };
	private String[] text = { "进攻日志", "遇袭日志", "部队日志", "大战役日志" };

	private Integer[] descs = { UITextProp.BATTLE_LOG_DESC1,
			UITextProp.BATTLE_LOG_DESC2, UITextProp.BATTLE_LOG_DESC3,
			UITextProp.BATTLE_LOG_DESC4 };

	private OnClickListener[] onclick = { new OnClickListener() {

		@Override
		public void onClick(View v) {
			new LogAttackWindow().open();
		}
	}, new OnClickListener() {

		@Override
		public void onClick(View v) {
			new LogDefendWindow().open();

		}
	}, new OnClickListener() {

		@Override
		public void onClick(View v) {
			new LogTroopWindow().open();
		}
	}, new OnClickListener() {

		@Override
		public void onClick(View v) {
			new HotBattleLogWindow().open();
		}
	} };

}
