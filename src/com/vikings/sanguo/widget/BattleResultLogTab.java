/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2014-4-22 上午11:49
 *
 *  Author : danping peng
 *
 *  Comment : 
 */
package com.vikings.sanguo.widget;

import java.util.ArrayList;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.vikings.sanguo.R;
import com.vikings.sanguo.model.BattleLogInfoClient;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.utils.ViewUtil;

public class BattleResultLogTab {

	public BattleResultLogTab(BattleLogInfoClient blic, ViewGroup content) {
		LogAdapter adapter = new LogAdapter();
		ListView listView = (ListView) content.findViewById(R.id.listView);
		ArrayList<String> logs = blic.getLogList();
		adapter.addItems(logs);
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	class LogAdapter extends ObjectAdapter {

		@Override
		public void setViewDisplay(View v, Object o, int index) {
			String str = (String) getItem(index);
			ViewUtil.setRichText(v, str);
		}

		@Override
		public int getLayoutId() {
			return R.layout.battle_log_txt;
		}

	}
}
