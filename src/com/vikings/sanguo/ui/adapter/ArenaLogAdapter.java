/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-12-4 下午4:00:57
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.adapter;

import java.util.Date;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.invoker.BattleLogInfoInvoker;
import com.vikings.sanguo.model.ArenaLogInfoClient;
import com.vikings.sanguo.ui.window.BattleWindow;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class ArenaLogAdapter extends ObjectAdapter implements OnClickListener {
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = Config.getController().inflate(getLayoutId());
			ViewHolder holder = new ViewHolder();
			holder.logTitle = (TextView) convertView
					.findViewById(R.id.logTitle);
			holder.logDesc = (TextView) convertView.findViewById(R.id.logDesc);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			convertView.setTag(holder);
		}

		setViewDisplay(convertView, getItem(position), position);
		return convertView;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {
		ArenaLogInfoClient alic = (ArenaLogInfoClient) getItem(index);
		ViewHolder holder = (ViewHolder) v.getTag();
		holder.alic = alic;

		Date date = new Date(alic.getTime() * 1000L);
		ViewUtil.setRichText(holder.time, StringUtil.color(
				DateUtil.db2MinuteFormat.format(date), R.color.k7_color1));
		ViewUtil.setRichText(holder.logTitle, alic.getResult());
		ViewUtil.setRichText(holder.logDesc, alic.getDesc());

		v.setOnClickListener(this);
	}

	@Override
	public int getLayoutId() {
		return R.layout.battle_log_item;
	}

	class ViewHolder {
		TextView logTitle, logDesc, time;
		ArenaLogInfoClient alic;
	}

	@Override
	public void onClick(View v) {
		ViewHolder holder = (ViewHolder) v.getTag();
		if (holder.alic.isMeAtk()) {
			if (holder.alic.isMeWin()) {
				new ArenaLogBattleLogInvoker(holder.alic.getBattleLog(),
						holder.alic.getAttackerPos(),
						holder.alic.getDefenderPos()).start();
			} else {
				new ArenaLogBattleLogInvoker(holder.alic.getBattleLog(),
						holder.alic.getAttackerPos(),
						holder.alic.getAttackerPos()).start();
			}
		} else {
			if (holder.alic.isMeWin()) {
				new ArenaLogBattleLogInvoker(holder.alic.getBattleLog(),
						holder.alic.getDefenderPos(),
						holder.alic.getDefenderPos()).start();
			} else {
				new ArenaLogBattleLogInvoker(holder.alic.getBattleLog(),
						holder.alic.getDefenderPos(),
						holder.alic.getAttackerPos()).start();
			}
		}

	}

	private class ArenaLogBattleLogInvoker extends BattleLogInfoInvoker {
		private int oldRank;
		private int newRank;

		public ArenaLogBattleLogInvoker(long battleLogId, int oldRank,
				int newRank) {
			super(battleLogId);
			this.oldRank = oldRank;
			this.newRank = newRank;
		}

		@Override
		protected void onOK() {
			new BattleWindow().open(battleDriver, null, true, oldRank, newRank);
		}

	}
}
