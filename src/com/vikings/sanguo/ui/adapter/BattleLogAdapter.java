package com.vikings.sanguo.ui.adapter;

import java.util.Date;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BattleLogClient;
import com.vikings.sanguo.thread.AddrMutiLoader;
import com.vikings.sanguo.ui.alert.HistoryBattleLogTip;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class BattleLogAdapter extends ObjectAdapter {
	@Override
	public void setViewDisplay(View v, Object o, int index) {

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.logTitle = (TextView) convertView
					.findViewById(R.id.logTitle);
			holder.logDesc = (TextView) convertView.findViewById(R.id.logDesc);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		BattleLogClient blc = (BattleLogClient) getItem(position);

		Date date = new Date(blc.getTime() * 1000L);
		ViewUtil.setText(holder.time, DateUtil.db2MinuteFormat.format(date));
		
		if (blc.getTilesTitle() != null && blc.getTilesTitle().length > 0) {
			new AddrMutiLoader(blc.getTilesTitle(), holder.logTitle,
					blc.getTitle());
		} else {
			ViewUtil.setRichText(holder.logTitle, blc.getTitle());
		}

		new AddrMutiLoader(blc.getTiles(), holder.logDesc, blc.getText());
		convertView.setOnClickListener(new ClickListener(blc.getType(), blc));
		return convertView;
	}

	@Override
	public int getLayoutId() {
		return R.layout.battle_log_item;
	}

	private static class ViewHolder {
		TextView logTitle, logDesc, time;
	}

	private class ClickListener implements OnClickListener {
		private int type;
		private BattleLogClient blc;

		public ClickListener(int type, BattleLogClient blc) {
			this.type = type;
			this.blc = blc;
		}

		@Override
		public void onClick(View v) {
			if (type == 1)
				new HistoryBattleLogTip(blc).show();
			else
				Config.getController().alert(
						((TextView) v.findViewById(R.id.logDesc)).getText()
								.toString());
		}
	}
}
